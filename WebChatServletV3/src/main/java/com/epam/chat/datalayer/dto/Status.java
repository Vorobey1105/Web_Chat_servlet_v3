package com.epam.chat.datalayer.dto;

/**
 * Status object class.
 * 
 * @author Sergey Vorobyev	
 * @since 09/27/2019
 * @version 1.0
 * 
 */
public class Status {
    private String statusName;
    private String description;

    public String getStatus() {
	return statusName;
    }

    public void setStatus(String status) {
	this.statusName = status;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @Override
    public String toString() {
	return "Statuses [status=" + statusName + ", "
		+ "description=" + description + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((statusName == null) 
		? 0 : statusName.hashCode());
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
	Status other = (Status) obj;
	if (statusName == null) {
	    if (other.statusName != null)
		return false;
	} else if (!statusName.equals(other.statusName))
	    return false;
	return true;
    }
}
