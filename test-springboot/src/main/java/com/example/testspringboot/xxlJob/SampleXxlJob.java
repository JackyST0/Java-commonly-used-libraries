package com.example.testspringboot.xxlJob;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/2/10 11:26
 */
@Component
public class SampleXxlJob {

    @XxlJob("sampleJobHandler")
    public void sampleJobHandler() {
        System.out.println("XXL-JOB, Hello World.");
    }
}
