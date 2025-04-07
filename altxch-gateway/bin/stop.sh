#!/bin/bash

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "$0")"; pwd)"

# 设置 PID 文件路径
PID_FILE="$SCRIPT_DIR/app.pid"

# 检查 PID 文件是否存在
if [ ! -f "$PID_FILE" ]; then
    echo "PID file not found: $PID_FILE"
    exit 1
fi

# 读取 PID 文件中的进程 ID
PID=$(cat "$PID_FILE")

# 检查进程是否存在
if ps -p "$PID" > /dev/null; then
    echo "Stopping Altxch Cloud Application with PID: $PID"
    kill "$PID"  # 发送终止信号
    sleep 2  # 等待进程停止

    # 检查进程是否已停止
    if ps -p "$PID" > /dev/null; then
        echo "Failed to stop the application. Trying force kill..."
        kill -9 "$PID"  # 强制终止进程
        sleep 1
        if ps -p "$PID" > /dev/null; then
            echo "Force kill failed. Process may still be running."
            exit 1
        else
            echo "Application stopped successfully."
            rm -f "$PID_FILE"  # 删除 PID 文件
        fi
    else
        echo "Application stopped successfully."
        rm -f "$PID_FILE"  # 删除 PID 文件
    fi
else
    echo "No running application found with PID: $PID"
    rm -f "$PID_FILE"  # 删除 PID 文件
fi