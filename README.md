# ch dev

## Domain

![](https://github.com/catchchen/Proj-ch/blob/939c1021b6b3b3d495c5d8b01e681650e8e722e3/knowledge-share-domain_model.png)


https://www.jb51.net/article/168079.htm

# Restful架构风格

>  REST（Representational State Transfer）表述性状态转换，**REST指的是一组架构约束条件和原则**

URI 表示资源，一般对应服务器端领域模型中的实体类。

```json
{
  "success":true,
  "data": {
    "id": 1,
    "name": "xiaotuan"},
}
   
```

| ·         | response 格式  |
| --------- | -------------- |
| GET       | 单个对象、集合 |
| POST      | 新增成功的对象 |
| PUT/PATCH | 更新成功的对象 |
| DELETE    | 空             |

json格式约定

1. 时间用长整形(毫秒数)，客户端自己按需解析（[moment.js](http://mementjs.com/)）
2. 不传`null`字段

分页response

```json
{
    "paging":{"limit":10,"offset":0,"total":729},
    "data":[{},{},{}...]
}
```

