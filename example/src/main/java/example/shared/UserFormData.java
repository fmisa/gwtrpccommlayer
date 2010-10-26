package example.shared;

import java.io.Serializable;

public class UserFormData implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8480133366196663439L;

	String accountId;
	String name;
	Integer age;
	
	public UserFormData()
	{
	}

	public String toString()
	{
		return "accountId='" + getAccountId() + "' name='" + getName() + "' age=" + getAge();
	}
	
	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getAge()
	{
		return age;
	}

	public void setAge(Integer age)
	{
		this.age = age;
	}
}
