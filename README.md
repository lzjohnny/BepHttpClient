# 轻量级Android网络请求框架构成：

## BepHttpClient类
构建HttpURLConnection对象、设置参数、发起HTTP请求，传入BaseProcessor或其子类来处理HTTP响应。

- GET方法
- POST方法

## BaseProcessor类

 - onStart、onProcess、onFinish、onSuccess、onFailure五个回调方法。
 - 其中onSuccess、onFailure为抽象方法，使用者必须去实现，在不同的回调方法中做出针对性的操作（如根据响应内容更新UI等）。
 - 框架在不同阶段回调相应的方法。请求成功后（20X）调用onStart方法，参数为包含HTTP响应实体信息（类型和长度）的对象。
 - 开始接收数据时调用onSuccess方法，参数即为HTTP响应实体内容。
 - 发生异常时调用onFailure方法。

## TextProcessor类

 - BaseProcessor的子类，在父类基础上封装了byte[]转String的方法。

## 使用示例：GET：

```java

BepHttpClient httpClient = new BepHttpClient(url);
httpClient.get(new TextProcessor() {
	@Override
	public void onStart(ContentHeaderField field) {
		Log.d("MainActivity", field.getContentType());
		Log.d("MainActivity", Integer.toString(field.getContentLength()));
	}

	@Override
	public void onProcess() {
	}

	@Override
	public void onFinish() {

	}

	@Override
	public void onSuccess(String responseText) {
		Log.d("MainActivity", responseText);
	}

	@Override
	public void onFailure(String failInfo) {

	}
});

```

##使用示例：POST：

```java
BepHttpClient httpClient = new BepHttpClient(url);
// 添加首部参数（可选操作）
httpUtils.addHeaderParams("User-Agent", "Mozilla/5.0");
httpUtils.addHeaderParams("Connection", "keep-alive");

// 添加POST请求内容
Map<String, String> map = new HashMap<String, String>();
map.put("username", "admin");
map.put("password", "admin");

httpClient.post(map, new TextProcessor() {
	@Override
	public void onStart(ContentHeaderField field) {
		Log.d("MainActivity", field.getContentType());
		Log.d("MainActivity", Integer.toString(field.getContentLength()));
	}
	@Override
	public void onProcess() {

	}

	@Override
	public void onFinish() {

	}

	@Override
	public void onSuccess(String responseText) {
		Log.d("MainActivity", responseText);
	}

	@Override
	public void onFailure(String failInfo) {
		Log.d("Failure", failInfo);
	}
});
```


----------
**9.30 更新说明：大幅重构代码，完善了异步消息回调逻辑，增加详细注释。**