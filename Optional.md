# Java 8 Optional

### 简介
Optional是Java8提供的为了解决null安全问题的一个API。善用Optional可以使我们代码中很多繁琐、丑陋的设计就可以变得十分优雅，比如上面的真实业务场景的案例。

### 方法列表
按照常用的顺序排列如下：
- Optional.orElseGet(Supplier<? extends T> other) 如果存在则返回值，否则调用other并返回该调用的结果。
- Optional.orElse(T other) 如果存在返回，不存在返回other
- Optional.ofNullable(T value) value可以为空
- Optional.empty() 创建一个空实例对象，不存在值。不存在值的意义不是里面存null，null也是值。
- Optional.of(T value) 返回一个包含value的对象。value不能是null，否则会抛NPE异常
- Optional.get() 返回非空值，如果value为空，抛出NoSuchElementException
- Optional.isPresent() 值存在返回true，不存在返回false，存在null，也是false
- Optional.ifPresent(Consumer<? super T> consumer) 如果存在值，则使用该值调用指定的消费者，否则什么都不做。
- Optional.filter(Predicate<? super T> predicate) 如果存在一个值，并且该值与给定的Predicate匹配，则返回描述该值的Optional，否则返回空Optional。
- Optional.map(Function<? super T, ? extends U> mapper) 如果存在值，则对其应用所提供的映射函数，如果结果非空，则返回描述结果的Optional。否则返回一个空的Optional。
- Optional.flatMap(Function<? super T, Optional> mapper) 如果存在值，则对其应用所提供的与Optional相关的映射函数，返回该结果，否则返回空的Optional。这个方法类似于map(Function)，但是所提供的映射器的结果已经是一个Optional，并且如果调用，flatMap不会用额外的Optional来包装它。
- Optional.orElseThrow(Supplier<? extends X> exceptionSupplier) throws X 返回包含的值(如果存在)，否则抛出由提供的提供程序创建的异常。

### 方法示例
- 创建Optional对象
    - 创建个空对象
        ```
        Optional<String> empty = Optional.empty();
        System.out.println(empty.isPresent());  // false
        System.out.println(empty.orElse("hello"));  // hello
        System.out.println(empty);  // Optional.empty
        ```
    - 创建一个非空的 Optional 对象
        ```
        Optional<String> opt = Optional.of("hello world");
        System.out.println(opt); // 输出：Optional[hello world]
        ```
    - 可以使用静态方法 ofNullable() 创建一个即可空又可非空的 Optional 对象
        ```
        Optional<String> optOrNull = Optional.ofNullable(null);
        System.out.println(optOrNull); // 输出：Optional.empty
        ```

- 判断值是否存在  
可以通过方法 isPresent() 判断一个 Optional 对象是否存在，如果存在，该方法返回 true，否则返回 false——取代了 obj != null 的判断。
    ```
    Optional<String> opt = Optional.of("hello world");
    System.out.println(opt.isPresent()); // 输出：true

    Optional<String> optOrNull = Optional.ofNullable(null);
    System.out.println(optOrNull.isPresent()); // 输出：false
    ```

- 非空表达式  
Optional 类有一个非常现代化的方法——ifPresent()，允许我们使用函数式编程的方式执行一些代码，因此，我把它称为非空表达式。如果没有该方法的话，我们通常需要先通过 isPresent() 方法对 Optional 对象进行判空后再执行相应的代码。有了 ifPresent() 之后，情况就完全不同了，可以直接将 Lambda 表达式传递给该方法，代码更加简洁，更加直观：
    ```
    Optional<String> optOrNull = Optional.ofNullable(null);
    if (optOrNull.isPresent()) {
        System.out.println(optOrNull.get().length());
    }

    Optional<String> opt = Optional.of("hello world");
    opt.ifPresent(str -> System.out.println(str.length())); // 如果是null这里就不会执行了
    ```

- 设置（获取）默认值  
有时候，我们在创建（获取） Optional 对象的时候，需要一个默认值，orElse() 和 orElseGet() 方法就派上用场了。这两个方法都可以用来提供一个默认值，当Optional对象为空时返回这个默认值。但是他们的工作方式有所不同。  
orElse方法无论Optional对象是否为空，都会执行传入的方法或计算表达式。所以在这个例子中，即使name不为空，orElse方法也会执行getDefaultValue方法。  
orElseGet方法只有在Optional对象为空时才会执行传入的Supplier（这里是Tests::getDefaultValue方法）。所以在这个例子中，因为name不为空，orElseGet方法不会执行getDefaultValue方法。
    ```
    public static void main(String[] args) {
        String name = "hello";
        System.out.println("orElse");
        String name2 = Optional.ofNullable(name).orElse(getDefaultValue());

        System.out.println("orElseGet");
        String name3 = Optional.ofNullable(name).orElseGet(TaskCancelServiceImpl::getDefaultValue);
    }

    public static String getDefaultValue() {
        System.out.println("getDefaultValue");
        return "getDefaultValue";
    }
    ```

- 获取值  
直观从语义上来看，get() 方法才是最正宗的获取 Optional 对象值的方法，但很遗憾，该方法是有缺陷的，因为假如 Optional 对象的值为 null，该方法会抛出 NoSuchElementException 异常。这完全与我们使用 Optional 类的初衷相悖，所以不建议使用get方法。
    ```
    String name = null;
    Optional<String> optOrNull = Optional.ofNullable(name);
    System.out.println(optOrNull.get());
    ```

- 过滤Filter使用  
使用Optional可以用来检验参数的合法性
    ```
    String name = "hello";
    name = Optional.ofNullable(name)
            .filter(name::startsWith)
            .orElseThrow(()->new IllegalArgumentException("Invalid username."));
    System.out.println(name);
    ```
