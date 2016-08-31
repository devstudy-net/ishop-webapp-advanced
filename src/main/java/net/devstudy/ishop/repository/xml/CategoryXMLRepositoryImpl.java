package net.devstudy.ishop.repository.xml;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import net.devstudy.ishop.entity.Category;
import net.devstudy.ishop.exception.InternalServerErrorException;
import net.devstudy.ishop.repository.CategoryRepository;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class CategoryXMLRepositoryImpl implements CategoryRepository {
	private final String fileName;

	public CategoryXMLRepositoryImpl(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public List<Category> listAllCategories() { 
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Categories.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Categories categories = (Categories) jaxbUnmarshaller.unmarshal(new File(fileName));
			return categories.getCategory();
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Can't read categories from file: " + fileName, e);
		}
	}

	@XmlRootElement(name = "categories")
	private static class Categories {
		private List<Category> category;
		public List<Category> getCategory() {
			return category;
		}
		@SuppressWarnings("unused")
		public void setCategory(List<Category> category) {
			this.category = category;
		}
	}
}
