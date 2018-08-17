package com.envisioniot.enos.iot_mqtt_sdk.core.exception;

/**
 *
 * @author zhensheng.cai
 * @date 2018/7/3
 */
public class EnvisionException extends Exception
{
	private static final long serialVersionUID = 5874811335473710877L;
	private int errorCode;
	private String errorMessage;

	public EnvisionException(int errorCode, String errorMessage)
	{
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public EnvisionException(Throwable cause, int errorCode, String errorMessage)
	{
		super(cause);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public EnvisionException(String message, Throwable cause, int errorCode, String errorMessage)
	{
		super(message, cause);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public EnvisionException(String message, Throwable cause, EnvisionError error)
	{
		super(message, cause);
		this.errorCode = error.getErrorCode();
		this.errorMessage = error.getErrorMessage();
	}

	public EnvisionException(Throwable cause, EnvisionError error)
	{
		super(cause);
		this.errorCode = error.getErrorCode();
		this.errorMessage = error.getErrorMessage();
	}

	public EnvisionException(EnvisionError error)
	{
		this.errorCode = error.getErrorCode();
		this.errorMessage = error.getErrorMessage();
	}

	public EnvisionException(EnvisionError error, String extraMsg)
	{
		this.errorCode = error.getErrorCode();
		this.errorMessage = error.getErrorMessage() + " " + extraMsg;
	}

	public int getErrorCode()
	{
		return errorCode;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}
	
}
