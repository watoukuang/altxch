#!/bin/bash
# 检查端口是否被占用
PORT=8080

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "$0")"; pwd)"

# 设置 Java 选项
JAVA_OPTS="-Xms512m -Xmx512m"

# 设置主类
MAIN_CLASS="com.watoukuang.altxch.gateway.AltxchGatewayApplication"

# 设置 JAR 文件和 lib 目录
JAR_FILE="$SCRIPT_DIR/../altxch-gateway.jar"
LIB_DIR="$SCRIPT_DIR/../lib/*"

# 设置 PID 文件路径
PID_FILE="$SCRIPT_DIR/app.pid"

# 检查 JAR 文件和 lib 目录是否存在
if [ ! -f "$JAR_FILE" ]; then
    echo "JAR file not found: $JAR_FILE"
    exit 1
fi

if [ ! -d "$SCRIPT_DIR/../lib" ]; then
    echo "Library directory not found: $SCRIPT_DIR/../lib"
    exit 1
fi

if lsof -i:$PORT > /dev/null; then
    echo "Port $PORT is already in use. Cannot start the application."
    exit 1
fi

# 启动命令
nohup java $JAVA_OPTS -cp "$JAR_FILE:$LIB_DIR" $MAIN_CLASS > startup.log 2>&1 &

# 输出进程 ID，并将其保存到 PID 文件
PID=$!
echo $PID > "$PID_FILE"

# 检查进程是否启动成功
sleep 1  # 等待一秒钟以确保进程有时间启动
if ps -p $PID > /dev/null; then
    echo "Altxch Gateway Application started successfully with PID: $PID"
else
    echo "Failed to start Altxch Gateway Application."
    rm -f "$PID_FILE"  # 删除 PID 文件
fi