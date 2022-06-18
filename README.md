# 数据源仓库

提交格式：将自制数据源整合到一个文件夹，打包好的ads文件放到此文件夹内，然后将这个文件夹提交到对应接口文件夹中，并在提交PR时给出**数据源名称**、**数据源ads地址**、**描述**、**作者**、**图标（可选）**。

例：datasource/202202121320/sample1

datasource为仓库目录内的文件夹。

202202121320为**接口版本文件夹**，所有此版本的数据源都在文件夹下。

sample1为数据源文件夹，**名称由制作者自拟**。此文件夹内具体格式不限，但要保证含有**源码**及**ads文件**即可。

## 数据源列表

| 名称    | 接口版本     | 描述    | 分类  |
| ------- | ------------ | ------- | ------- |
| sample1 | [202202121320](datasource/202202121320/sample1)、[202204191624](datasource/202204191624/sample1)、[202205211312](datasource/202205211312/sample1) | 数据源1 | 动漫 |
| sample2 | [202202121320](datasource/202202121320/sample2)、[202204191624](datasource/202204191624/sample2)、[202205211312](datasource/202205211312/sample2) | 数据源2 | 动漫 |
| henniu-sese | [202202121320](datasource/202202121320/mr-mihu) | 很牛视频源🔞 | 18+ |
| oyyds | [202205211312](datasource/202205211312/oyyds) | 动漫汇集 | 动漫 |

## 开发说明

请将生成的ads数据源文件提交PR至本仓库

开发者要在相应接口版本的文件夹下修改data_source_list.json文件，注意格式正确性。

### data_source_list.json字段说明

|                  | 描述                                         | 数据类型 | 是否可以不写 | 备注                                                         |
| ---------------- | -------------------------------------------- | -------- | ------------ | ------------------------------------------------------------ |
| name             | 数据源**名称**，是**唯一标识**，**不能重复** | 字符串   | 否           |                                                              |
| interfaceVersion | 数据源的**接口版本**                         | 字符串   | 否           |                                                              |
| versionName      | 数据源**版本名**，给**用户**看的             | 字符串   | 否           |                                                              |
| versionCode      | 数据源**版本号**，给**程序**使用的           | 整型     | 否           |                                                              |
| author           | **作者**                                     | 字符串   | 否           |                                                              |
| icon             | 数据源**图标URL地址**，**可以不写**此字段    | 字符串   | 是           | 若文件在本仓库，可填写相对地址，如"/datasource/接口版本号/xxx/icon.png" |
| describe         | 描述**简介**等                               | 字符串   | 否           |                                                              |
| publicAt         | **发布时间**                                 | 长整型   | 否           |                                                              |
| downloadUrl      | **ads文件**下载地址**URL**                   | 字符串   | 否           | 若文件在本仓库，可填写相对地址，如"/datasource/接口版本号/xxx/xxx.ads" |

### Json举例

```json
{
  "dataSourceList": [
    {
      "name": "CustomDataSource1",
      "interfaceVersion": "202205211312",
      "versionName": "1.2.2",
      "versionCode": 9,
      "author": "默认",
      "icon": "/datasource/202205211312/sample1/icon.webp",
      "describe": "动漫",
      "publicAt": 1653113783000,
      "downloadUrl": "/datasource/202205211312/sample1/CustomDataSource1.ads"
    }
  ]
}
```

