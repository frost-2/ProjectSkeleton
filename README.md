# ProjectSkeleton
本项目为基于springboot搭建的后端接口项目骨架，将一些在每个项目都必须的功能封装好，方便我们新建项目时直接引用。

具体如下：
- 接口同一返回JSON如下格式数据。
```json
{
  "code": 0,
  "msg": "string",
  "result": {
    "date": {},
    "row": 0
  }
}
```
- 接口采用swagger编写在线接口文档。
- 基于拦截器实现CROS跨域配置
- 基于拦截器实现同一接口校验
- 全局异常处理机制
- SpringAop配置
- Redis多数据源配置
- Mysql多数据源配置
- Quartz定时任务
- RBAC权限管理(进行中。。。)