console.log("Frida脚本已成功注入!");

// 使用setImmediate确保在主线程执行
setImmediate(function() {
    Java.perform(function() {
        console.log("Java VM已附加");
        console.log("===============================")
        // 获取BtnActivity类
        var BtnActivity = Java.use("com.example.myapplication.BtnActivity");
        
        // Hook Sub方法
        BtnActivity.Sub.implementation = function(a, b) {
            console.log("[*] Sub函数被调用");
            
            var originalA = a;
            var originalB = b;
            a = 10;
            b = 4;
            
            console.log("[+] Before : a=" + originalA + ", b=" + originalB);
            console.log("[+] After  : a=" + a + ", b=" + b);
            
            var originalResult = this.Sub(a, b);
            console.log("[-] Hook传参 从而 修改方法返回值 ---> " + originalResult);
            
            var newResult = 999;
            console.log("[-] 直接 Hook 返回值 ---> " + newResult);
            console.log("===============================")
            
            return newResult;
        };
        
        // hook native方法
        BtnActivity.add.implementation = function(a, b, c) {
            console.log("[*] Native add函数被调用");
            
            var originalA = a;
            var originalB = b;
            var originalC = c;
            a = 10;
            b = 4;
            c = 3;
    
            console.log("[+] Before : a=" + originalA + ", b=" + originalB + ", c=" + originalC);
            console.log("[+] After  : a=" + a + ", b=" + b + ", c=" + c);
    
            // 调用原始native方法
            var result = this.add(a, b, c);
            console.log("[-] Hook传参 从而 修改方法返回值 ---> " + result);
            
            // 修改native方法返回值
            var hookedResult = 887;
            console.log("[-] 直接 Hook 返回值 ---> " + hookedResult);
            console.log("===============================")
            return hookedResult;
        };


        // Hook Calculator接口的实现类
        var SimpleCalculator = Java.use("com.example.myapplication.BtnActivity$SimpleCalculator");
        SimpleCalculator.calculate.implementation = function(a, b) {
            console.log("[*] SimpleCalculator.calculate被调用");
            /* 
            修改传参和返回值省略,与Sub方法一样
            */ 
            return 999;
        };


        
        //  Hook StaticUsed类（静态内部类）
        var StaticUsed = Java.use("com.example.myapplication.BtnActivity$StaticUsed");
        // Hook构造函数
        StaticUsed.$init.implementation = function(name2) {
            console.log("[*] StaticUsed构造函数被调用");
            
            var originalName = name2;
            name2 = "Hook修改的名字";
            
            console.log("[+] Before : name2=" + originalName);
            console.log("[+] After  : name2=" + name2);
            
            // 调用原始构造函数
            this.$init(name2);
            console.log("===============================");
        };
        
        // Hook getName方法 (实例方法)
        StaticUsed.getName.implementation = function() {
            console.log("[*] StaticUsed.getName被调用");
            
            // 获取静态变量值
            var name1 = StaticUsed.name1.value;
            // 获取实例变量值
            var name2 = this.name2.value;
            
            console.log("[+] 静态变量 name1: " + name1);
            console.log("[+] 实例变量 name2: " + name2);
            
            var originalResult = this.getName();
            console.log("[-] 原始结果: " + originalResult);
            
            var hookedResult = "Hook修改的名称: " + name1 + "_" + name2;
            console.log("[-] Hook后结果: " + hookedResult);
            console.log("===============================");
            
            return hookedResult;
        };
        
        // Hook mul重载方法(两个参数) - 静态方法
        StaticUsed.mul.overload('int', 'int').implementation = function(a, b) {
            console.log("[*] StaticUsed.mul(a,b)被调用");
            
            var originalA = a;
            var originalB = b;
            a = 5;
            b = 6;
            
            console.log("[+] Before : a=" + originalA + ", b=" + originalB);
            console.log("[+] After  : a=" + a + ", b=" + b);
            
            // 调用静态方法
            var result = StaticUsed.mul(a, b);
            console.log("[-] 原始结果: " + result);
            
            var hookedResult = 12345;
            console.log("[-] Hook后结果: " + hookedResult);
            console.log("===============================");
            
            return hookedResult;
        };
        
        // Hook mul重载方法(三个参数) - 静态方法
        StaticUsed.mul.overload('int', 'int', 'int').implementation = function(a, b, c) {
            console.log("[*] StaticUsed.mul(a,b,c)被调用");
            
            var originalA = a;
            var originalB = b;
            var originalC = c;
            a = 2;
            b = 3;
            c = 4;
            
            console.log("[+] Before : a=" + originalA + ", b=" + originalB + ", c=" + originalC);
            console.log("[+] After  : a=" + a + ", b=" + b + ", c=" + c);
            
            // 调用静态方法
            var result = StaticUsed.mul(a, b, c);
            console.log("[-] 原始结果: " + result);
            
            var hookedResult = 54321;
            console.log("[-] Hook后结果: " + hookedResult);
            console.log("===============================");
            
            return hookedResult;
        };
            


        //  Hook CheckUsed类（非静态内部类）
        var CheckUsed = Java.use("com.example.myapplication.BtnActivity$CheckUsed");
        

        // Hook构造函数 - 非静态内部类需要外部类实例作为参数
        CheckUsed.$init.implementation = function(btnActivity) {
            console.log("[*] CheckUsed构造函数被调用");
            
            // 调用原始构造函数，传入外部类实例
            this.$init(btnActivity);
            
            // 修改私有成员变量
            this.threshold.value = 100;
            console.log("[+] 修改private threshold值为: " + this.threshold.value);
            console.log("===============================");
        };
        
        // Hook check方法
        CheckUsed.check.implementation = function(value) {
            console.log("[*] CheckUsed.check被调用");
            
            var originalValue = value;
            value = 50;
            
            console.log("[+] Before : value=" + originalValue);
            console.log("[+] After  : value=" + value);
            
            var result = this.check(value);
            console.log("[-] 原始结果: " + result);
            
            var hookedResult = "Hook修改的结果: value=" + value + " threshold=" + this.threshold.value;
            console.log("[-] Hook后结果: " + hookedResult);
            console.log("===============================");
            
            return hookedResult;
        };


        // Hook并调用StaticNotUsed类（从未被调用的静态内部类）
        var StaticNotUsed = Java.use("com.example.myapplication.BtnActivity$StaticNotUsed");
        
        // 创建实例并调用方法
        Java.perform(function() {
            try {
                console.log("[*] 开始调用StaticNotUsed类");
                
                // 创建实例
                var instance = StaticNotUsed.$new("hello");
                console.log("[+] 成功创建StaticNotUsed实例");
                
                // 调用getName方法
                var result = instance.getName();
                console.log("[+] getName方法返回结果: " + result);
                console.log("===============================");
            } catch(e) {
                console.log("[-] 调用StaticNotUsed类时出错: " + e);
            }
        });


        // 打印更多类信息
        console.log("[*] 类的完整信息:");
        var BTNClass = Java.cast(BtnActivity.class, Java.use("java.lang.Class"));
        console.log("[+] 实现的接口: ", BTNClass.getInterfaces());
        console.log("[+] 父类: ", BTNClass.getSuperclass());
    });
});

