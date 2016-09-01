package net.devstudy.ishop.entity;

import java.io.Serializable;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public abstract class AbstractEntity<T> implements Serializable {
	private static final long serialVersionUID = -774095045487801539L;

	public abstract T getId();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntity other = (AbstractEntity) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s [id=%s]", getClass().getSimpleName(), getId());
	}
}
