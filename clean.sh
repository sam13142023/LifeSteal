# 1. 删除所有历史，保留当前文件状态
git checkout --orphan temp_branch
git add .
git commit -m "Initial commit"

# 2. 删除旧分支并重命名新分支
git branch -D main       # 或 master，取决于你当前分支名
git branch -m main

# 3. 强制推送到远程仓库（清除历史）
git push -f origin main

# 4. 打上 v1.0.0 标签并推送
git tag v1.0.0
git push origin v1.0.0

