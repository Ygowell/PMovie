# PMovie
展示最新热播电影App

使用 themoviedb.org API
API 使用提示
你需要使用 themoviedb.org 提供的 API获取热门电影列表。
1. 如果你尚未注册账号，那么需要注册账号并申请 API 密钥。在申请中注明你的用途是教育
/非商业用途。你还需要提供一些个人信息。提交完申请后，很快就会收到电子邮件查看密
钥。
2. 要获得热门电影列表，你需要从 /movie/popular 和 /movie/top_rated 中请求数据。并提供
API 密钥。
3. 将密钥添加到 HTTP 请求最后，作为 URL 参数，如下所示
http://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]
4. 从请求的返回结果中提取出电影 ID。在后续请求中，你会用到这个ID。

#替换key
将gradle.properties中的MyOpenMovieApiKey替换成你自己的才能运行