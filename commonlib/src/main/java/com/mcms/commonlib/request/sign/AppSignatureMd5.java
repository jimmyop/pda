
package com.mcms.commonlib.request.sign;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * App 访问签名处理类。 <br>
 * 签名变量名默认为：sign <br>
 * 业务参数变量名默认为：params <br>
 * 
 * 生成签名直接调用 sign() 方法。该方法首先排除不需要签名参数，包括sign字段，然后生成签名。 <br>
 * 验证签名的合法性使用verifySign()。 <br>
 * 
 * @author 丁伟
 * 
 */
@SuppressWarnings({"rawtypes" })
public class AppSignatureMd5 {

	private final String secretKey; // 签名密钥
	private final Map<String, String> parameters; // 参数的Map格式
	
	private String urlEncoding = "UTF8"; //url编码类型
	
	private String bizParamsName = "params";
	private String signParamName = "sign";
	private ArrayList<String> ignoreParams = new ArrayList<String>(4);
	private String signStr;

	/**
	 * 构造签名对象
	 * 
	 * @param secretKey
	 *            签名密钥
	 * @param parameters
	 *            签名参数的Map对象。key=参数名，value=值,不能存在同名参数。<br>
	 *            注意：params JSON 的业务参数传递原始字符串，是经过URL解码之后的内容。<br>
	 *            不能将JSON参数格式化为Map对象，传递字符串就可以了。
	 */
	public AppSignatureMd5(String secretKey, Map<String, String> parameters) {
		this.secretKey = secretKey;
		this.parameters = parameters == null ? new HashMap<String, String>() : parameters;
		addIgnoreSignParam(signParamName);
	}

	/**
	 * 构造签名处理对象
	 * 
	 * @param secretKey
	 *            签名密钥
	 * @param queryString
	 *            查询字符串。<br>
	 *            注意：<br>
	 *            1.不处理参数重名情况。<br>
	 *            2.查询字符串必须是经过URL编码之后的内容，默认解码方式UTF8
	 */
	public AppSignatureMd5(String secretKey, String queryString) {
		this(secretKey, queryString, "UTF8");
	}
	
	/**
	 * 构造签名处理对象
	 * 
	 * @param secretKey
	 *            签名密钥
	 * @param queryString
	 *            查询字符串。<br>
	 *            注意：<br>
	 *            1.不处理参数重名情况。<br>
	 *            2.查询字符串必须是经过URL编码之后的内容，默认解码方式UTF8
	 * @param urlEncoding Url解码方式
	 */
	public AppSignatureMd5(String secretKey, String queryString, String urlEncoding) {
		this.urlEncoding = urlEncoding;
		this.secretKey = secretKey;
		Map<String, String> parameters = new QueryStringToMap(this.urlEncoding, queryString).toMap();
		this.parameters = parameters == null ? new HashMap<String, String>() : parameters; 
		addIgnoreSignParam(signParamName);
	}

	/**
	 * 验证签名.验证访问请求的数据是否合法
	 * 
	 * @return 验证成功返回true，验证失败返回false
	 */
	public boolean verifySign() {
		String originalSign = getSignInParams();
		return this.sign().equals(originalSign);
	}

	/**
	 * 获取访问请求中的签名结果。如果原请求中不存在签名信息，则返回""
	 * 
	 * @return
	 */
	private String getSignInParams() {

		if (parameters.containsKey(signParamName)) {
			return parameters.get(signParamName);
		}
		return "";
	}

	/**
	 * 产生签名
	 * 
	 * @return
	 */
	public String sign() {
		Map<String, String> signParams = buildSignParamsAndSort();
		String originalString = buildSignatureOriginalString(signParams);
		originalString += secretKey;		
		this.signStr = Md5Util.md5(originalString);
		return signStr;
	}
	
	public String singFail(){
		   return "sing Params:"+parameters.toString()+"\n"+
		   "secretKey:"+this.secretKey+"\n"+
		   "new sing:"+this.signStr+"\n"+
		   "old sing:"+getSignInParams();
	   }

	/**
	 * 构造用于签名的参数列表。排除ignoreParams中列出的参数。
	 * 
	 * @return
	 */
	private Map<String, String> buildSignParamsAndSort() {
		Map<String, String> signParams = new KeySortedMap(parameters);
		signParams.putAll(parameters);
		for (String ignoreParam : ignoreParams) {
			signParams.remove(ignoreParam);
		}
		return signParams;
	}

	/**
	 * 构造签名原始字符串
	 * 
	 * @param signParams
	 * @return
	 */
	private String buildSignatureOriginalString(Map<String, String> signParams) {
		StringBuilder sb = new StringBuilder(128);
		Iterator keys = signParams.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String value = signParams.get(key);
			sb.append(key + "=");

			if (key.equals(bizParamsName)) {
				sb.append(buildBizSignatureOriginalString(value));
			} else {
				sb.append(value);
			}

			if (keys.hasNext()) {
				sb.append("&");
			}
		}

		return sb.toString();
	}

	/**
	 * 构造业务数据签名原始字符串
	 * 
	 * @return 排序后的JSON格式字符串
	 */
	public String buildBizSignatureOriginalString(String bizParamsString) {
		if (bizParamsString == null || bizParamsString.isEmpty()) {
			return "";
		}
		try {
			JSONObject jsonObj = new JSONObject(bizParamsString);
			return buildSortedJsonString(jsonObj);
		} catch (JSONException e) {
			System.out.println("JSON业务数据格式化错误");
			e.printStackTrace();
			return "";
		}
	}

	private String buildSortedJsonString(JSONObject jsonObj) {
		if (jsonObj == null) {
			return "";
		}
		StringBuilder result = new StringBuilder(64);
		result.append("{");

		Iterator keys = getSortKeys(jsonObj);
		while (keys.hasNext()) {
			String key = (String) keys.next();
			try {
				Object value = jsonObj.get(key);
				result.append("\"" + key + "\":");
				processJsonValue(result, value);
				if (keys.hasNext()) {
					result.append(",");
				}
			} catch (JSONException e) {
				assert false : "这里的程序不可能产生异常";
			}
		}
		result.append("}");
		return result.toString();
	}

	private Iterator getSortKeys(JSONObject jsonObj){
		Iterator<String> keys = jsonObj.keys();
		List<String> keyList = new ArrayList<>();
		if (keys != null){
			while (keys.hasNext()) {
				keyList.add(keys.next());
			}
		}
		Collections.sort(keyList);

		return keyList.iterator();
	}

	private void processJsonValue(StringBuilder result, Object value) {
		if (value instanceof String) {
			result.append("\"" + toJsonValue((String)value) + "\"");
		} else if (value instanceof Integer || value instanceof Double) {
			result.append(value);
		} else if (value instanceof JSONObject) {
			result.append(buildSortedJsonString((JSONObject) value));
		} else if (value instanceof Boolean) {
			result.append(value.toString());
		} else if (value instanceof JSONArray) {
			result.append("[");
			JSONArray jsonArray = (JSONArray) value;
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					processJsonValue(result, jsonArray.get(i));
				} catch (JSONException e) {
					assert false : "这里的程序不可能产生异常";
				}
				if (i < jsonArray.length() - 1) {
					result.append(",");
				}
			}
			result.append("]");
		} else {
			result.append(value.toString());
		}
	}

	private String toJsonValue(String value) {
		if (value == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder(value.length());
        for (char c : value.toCharArray())
        {
            switch (c)
            {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '"':
                    sb.append("\\\"");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if (c < 256)
                    {
                        sb.append(c);
                    }
                    else
                    {
                        sb.append(c);
                        //format.Format(_L("\\u%04x"), aString[i]);
                    }
                    break;
            }
        }
        return sb.toString();
	}

	
	public String getBizParamsName() {
		return bizParamsName;
	}

	public void setBizParamsName(String bizParamsName) {
		this.bizParamsName = bizParamsName;
	}

	public String getSignParamName() {
		return signParamName;
	}

	public void setSignParamName(String signParamName) {
		removeIgnoreSignParam(signParamName);
		addIgnoreSignParam(signParamName);
		this.signParamName = signParamName;
	}

	private void removeIgnoreSignParam(String signParamName) {
		int idx = indexOfIgnoreParams(signParamName);
		if (0 <= idx && idx < ignoreParams.size()) {
			this.ignoreParams.remove(idx);
		}
	}

	private int indexOfIgnoreParams(String paramName) {
		for (int i = 0; i < ignoreParams.size(); i++) {
			if (ignoreParams.get(i).equals(paramName)) {
				return i;
			}
		}
		return -1;
	}

	public AppSignatureMd5 addIgnoreSignParam(String ignoreParam) {
		ignoreParams.add(ignoreParam);
		return this;
	}

	public AppSignatureMd5 addIgnoreSignParam(List<String> ignoreParams) {
		ignoreParams.addAll(ignoreParams);
		return this;
	}

	/**
	 * 查询字符串转化为Map对象
	 * @author dingwei3
	 *
	 */
	private static class QueryStringToMap {
		
		private final String encoding;
		private final String queryString;
		
		public QueryStringToMap(String encoding, String queryString) {
			this.encoding = encoding;
			this.queryString = queryString;
		}

		public Map<String, String> toMap() {
			try {
				return parseQueryString();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}

		private Map<String, String> parseQueryString() throws UnsupportedEncodingException {
			Map<String, String> pParams = new HashMap<>();
			for (StringTokenizer st = new StringTokenizer(queryString, "&"); st.hasMoreTokens();) {
				String s = st.nextToken();
				parseParameter(pParams, s);
			}
			return pParams;
		}

		private void parseParameter(Map<String, String> pParams, String pParam) throws UnsupportedEncodingException {
			if (pParam.length() == 0) {
				return;
			}
			int offset = pParam.indexOf('=');
			final String name, value;
			if (offset == -1) {
				name = pParam;
				value = "";
			} else {
				name = pParam.substring(0, offset);
				value = pParam.substring(offset + 1);
			}
			addParameter(pParams, URLDecoder.decode(name, encoding), URLDecoder.decode(value, encoding));
		}

		private void addParameter(Map<String, String> pParams, String pKey, String pValue) {
			if (pParams.get(pKey) != null) {
				return;
			} 
			pParams.put(pKey, pValue);
		}

	}

}
