package net.devstudy.framework;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface SQLBuilder {

	SearchQuery build(Object... builderParams);
}
