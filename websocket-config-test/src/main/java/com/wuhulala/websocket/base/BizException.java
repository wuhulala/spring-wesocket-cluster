package com.wuhulala.websocket.base;

/**
 * 功能说明: 业务异常<br>
 * 注意事项: <br>
 * 系统版本: v1.0<br>
 * 开发人员: zjhua<br>
 * 开发时间: 20151123<br>
 */
@SuppressWarnings("serial")
public class BizException extends RuntimeException {

	private int errorNo;
	private Object[] errorParams;

	public BizException(int errorNo) {
		this.setErrorNo(errorNo);
	}

	public BizException(int errorNo, Object... errorParams) {
		this(errorNo);
		this.setErrorParams(errorParams);
	}

	public BizException(int errorNo, Throwable cause) {
		super(cause);
		this.setErrorNo(errorNo);
	}

	public BizException(int errorNo, Throwable cause, Object... errorParams) {
		super(cause);
		this.setErrorNo(errorNo);
		this.setErrorParams(errorParams);
	}

	public int getErrorNo() {
		return errorNo;
	}

	public void setErrorNo(int errorNo) {
		this.errorNo = errorNo;
	}

	public Object[] getErrorParams() {
		return errorParams;
	}

	public void setErrorParams(Object[] errorParams) {
//		if (ArrayUtils.isNotEmpty(errorParams)
//				&& (errorParams[0] instanceof Throwable)) {
//			super.initCause((Throwable) errorParams[0]);
//			errorParams = Arrays
//					.copyOfRange(errorParams, 1, errorParams.length);
//		}
//		this.errorParams = errorParams;
	}

	@Override
	public String getMessage() {
		// 如果是服务启动过程出错，可能缓存资源未加载，导致空指针，这里兼容处理，如果读取错误信息出现异常，则返回默认信息。
		try {
			return "==========================错误信息======================";
		} catch (Exception e) {
			return super.getMessage();
		}
	}

}