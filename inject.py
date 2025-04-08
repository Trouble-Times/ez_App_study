from struct import pack
import frida
import os

def on_message(message, data):
    if message['type'] == 'send':
        print(f"[JS] {message['payload']}")
    else:
        print(message)

def spawn_or_attach(device, package_name, app_name, switch_mode=1):
    """
    附加到进程或启动新进程
    switch_mode: 1=Spawn模式, 0=Attach模式
    返回: (pid, session)元组
    """
    pid = None
    
    # 1. Spawn启动新进程
    if switch_mode == 1:
        print(f"使用Spawn模式启动: {package_name}")
        pid = device.spawn(package_name)
    # 2. Attach已存在进程
    else:
        print(f"使用Attach模式查找: {app_name}")
        processes = device.enumerate_processes()
        for process in processes:
            if process.name == app_name:
                pid = process.pid
                print(f"找到进程: {process.name} (PID: {pid})")
                break
        
        if pid is None:
            raise Exception(f"未找到应用: {app_name}")

    try:
        print(f"附加到进程: PID {pid}")
        session = device.attach(pid)
        return pid, session
    except Exception as e:
        print(f"附加进程失败: {e}")
        exit(1)

def load_script(session, js_script_file):
    """
    加载并执行JS脚本
    返回: script对象
    """
    script_dir = os.path.dirname(os.path.abspath(__file__))
    js_path = os.path.join(script_dir, js_script_file)
    
    try:
        with open(js_path, "r", encoding="utf-8") as f:
            js_code = f.read()
        
        script = session.create_script(js_code)
        script.on('message', on_message)
        script.load()
        return script
    except Exception as e:
        print(f"加载脚本失败: {e}")
        exit(1)

if __name__ == "__main__":
    # 配置参数
    package_name = "com.example.myapplication"
    app_name = "My Application"
    js_script_file = "AndroidFridaHook.js"
    spawn_mode = 1  # 1=Spawn, 0=Attach
    
    try:
        # 连接设备
        device = frida.get_remote_device()
        
        # 附加到进程
        pid, session = spawn_or_attach(device, package_name, app_name, spawn_mode)
        
        # 加载脚本
        script = load_script(session, js_script_file)
        
        # 如果是Spawn模式，需要恢复执行
        if spawn_mode == 1:
            print("恢复进程执行...")
            device.resume(pid)
        
        # 保持连接
        print("脚本运行中,按Enter键退出...")
        input()
        
    except Exception as e:
        print(f"发生错误: {e}")