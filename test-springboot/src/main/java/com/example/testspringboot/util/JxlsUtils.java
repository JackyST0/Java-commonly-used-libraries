package com.example.testspringboot.util;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.internal.Engine;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.jxls.common.Context;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/4/18 17:48
 */
public class JxlsUtils{

    private static final String TEMPLATE_PATH="template";

    public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> model) throws IOException{
        Context context = new Context();
        if (model != null) {
            for (String key : model.keySet()) {
                context.putVar(key, model.get(key));
            }
        }
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer  = jxlsHelper.createTransformer(is, os);
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();
        Map<String, Object> funcs = new HashMap<>();
        funcs.put("utils", new JxlsUtils());    //添加自定义功能
        JexlBuilder jexlBuilder = new JexlBuilder();
        jexlBuilder.namespaces(funcs);
        JexlEngine engine= new Engine(jexlBuilder);
        evaluator.setJexlEngine(engine);
        jxlsHelper.processTemplate(context, transformer);
    }

    public static void exportExcel(File xls, File out, Map<String, Object> model) throws IOException {
        exportExcel(new FileInputStream(xls), new FileOutputStream(out), model);
    }

    public static void exportExcel(String templateName, OutputStream os, Map<String, Object> model) throws FileNotFoundException, IOException {
        File template = getTemplate(templateName);
        if(template!=null){
            exportExcel(new FileInputStream(template), os, model);
        }
    }


    //获取jxls模版文件

    public static File getTemplate(String name){
        String templatePath = JxlsUtils.class.getClassLoader().getResource(TEMPLATE_PATH).getPath();
        File template = new File(templatePath, name);
        if(template.exists()){
            return template;
        }
        return null;
    }

    // 日期格式化
    public String dateFmt(Date date, String fmt) {
        if (date == null) {
            return "";
        }
        try {
            return DateUtil.format(date, fmt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // if判断
    public Object ifelse(boolean b, Object o1, Object o2) {
        return b ? o1 : o2;
    }

}
