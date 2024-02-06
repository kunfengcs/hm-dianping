// let commonURL = "http://192.168.50.115:8081";
let commonURL = "/api";
// 设置后台服务地址
// 配置axios默认属性，设置服务端地址和超时时间
axios.defaults.baseURL = commonURL;
axios.defaults.timeout = 2000;

// request拦截器，用于在发送请求前将用户token放入请求头中
let token = sessionStorage.getItem("token");
axios.interceptors.request.use(
  config => {
    // 如果有token，则将token放入请求头的'authorization'字段中
    if(token) config.headers['authorization'] = token
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

// response拦截器，用于处理后台返回的数据
axios.interceptors.response.use(function (response) {
    // 判断执行结果，如果success字段为false，则抛出错误信息
  if (!response.data.success) {
    return Promise.reject(response.data.errorMsg)
  }
    // 返回处理后的数据
  return response.data;
}, function (error) {
    // 处理服务端异常或网络异常
  console.log(error)
    // 如果返回状态为401，表示未登录，跳转到登录页
  if(error.response.status == 401){
    // 未登录，跳转
    setTimeout(() => {
      location.href = "/login.html"
    }, 200);
    return Promise.reject("请先登录");
  }
    // 其他异常情况统一处理为"服务器异常"
  return Promise.reject("服务器异常");
});
// 设置axios的params序列化方法，用于将params对象转为URL参数字符串
axios.defaults.paramsSerializer = function(params) {
  let p = "";
  Object.keys(params).forEach(k => {
    // 将非空参数拼接成URL参数字符串
    if(params[k]){
      p = p + "&" + k + "=" + params[k]
    }
  })
  return p;
}

// 工具类对象，包含一些通用的函数和常量
const util = {
  commonURL, // 服务端地址常量
  getUrlParam(name) {
    // 获取URL中指定参数名的值
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    let r = window.location.search.substr(1).match(reg);
    if (r != null) {
      return decodeURI(r[2]);
    }
    return "";
  },
  formatPrice(val) {
    // 格式化价格，将价格转为整数
    if (typeof val === 'string') {
      if (isNaN(val)) {
        return null;
      }
      // 价格转为整数
      const index = val.lastIndexOf(".");
      let p = "";
      if (index < 0) {
        // 无小数
        p = val + "00";
      } else if (index === p.length - 2) {
        // 1位小数
        p = val.replace("\.", "") + "0";
      } else {
        // 2位小数
        p = val.replace("\.", "")
      }
      return parseInt(p);
    } else if (typeof val === 'number') {
      // 处理价格数字，转为整数
      if (!val) {
        return null;
      }
      const s = val + '';
      if (s.length === 0) {
        return "0.00";
      }
      if (s.length === 1) {
        return "0.0" + val;
      }
      if (s.length === 2) {
        return "0." + val;
      }
      const i = s.indexOf(".");
      if (i < 0) {
        return s.substring(0, s.length - 2) + "." + s.substring(s.length - 2)
      }
      const num = s.substring(0, i) + s.substring(i + 1);
      if (i === 1) {
        // 1位整数
        return "0.0" + num;
      }
      if (i === 2) {
        return "0." + num;
      }
      if (i > 2) {
        return num.substring(0, i - 2) + "." + num.substring(i - 2)
      }
    }
  }
}
