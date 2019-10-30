package com.epam.chat.datalayer.dto;

/**
 * User object class.
 * 
 * @author Sergey Vorobyev	
 * @since 09/27/2019
 * @version 1.0
 * 
 */
public class User {
    private String nick;
    private String role;
    private String fullName;
    private String telephoneNumber;
    private String email;
    private String password;

    public String getNick() {
	return nick;
    }

    public void setNick(String nick) {
	this.nick = nick;
    }

    public String getRole() {
	return role;
    }

    public void setRole(String role) {
	this.role = role;
    }

    public String getFullName() {
	return fullName;
    }

    public void setFullName(String fullName) {
	this.fullName = fullName;
    }

    public String getTelephoneNumber() {
	return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
	this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    @Override
    public String toString() {
	return "User [nick=" + nick + ", role=" + role + ", fullName=" 
		+ fullName + ", telephoneNumber=" + telephoneNumber + ", email=" 
		+ email + ", password=" + password + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((email == null) ? 0 : email.hashCode());
	result = prime * result + ((fullName == null) 
		? 0 : fullName.hashCode());
	result = prime * result + ((nick == null) ? 0 : nick.hashCode());
	result = prime * result + ((password == null) 
		? 0 : password.hashCode());
	result = prime * result + ((role == null) ? 0 : role.hashCode());
	result = prime * result + ((telephoneNumber == null) 
		? 0 : telephoneNumber.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	User other = (User) obj;
	if (email == null) {
	    if (other.email != null)
		return false;
	} else if (!email.equals(other.email))
	    return false;
	if (fullName == null) {
	    if (other.fullName != null)
		return false;
	} else if (!fullName.equals(other.fullName))
	    return false;
	if (nick == null) {
	    if (other.nick != null)
		return false;
	} else if (!nick.equals(other.nick))
	    return false;
	if (password == null) {
	    if (other.password != null)
		return false;
	} else if (!password.equals(other.password))
	    return false;
	if (role == null) {
	    if (other.role != null)
		return false;
	} else if (!role.equals(other.role))
	    return false;
	if (telephoneNumber == null) {
	    if (other.telephoneNumber != null)
		return false;
	} else if (!telephoneNumber.equals(other.telephoneNumber))
	    return false;
	return true;
    }
}
