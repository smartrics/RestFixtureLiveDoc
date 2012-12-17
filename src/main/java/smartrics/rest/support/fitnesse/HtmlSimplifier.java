package smartrics.rest.support.fitnesse;

import java.io.IOException;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CleanerTransformations;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagTransformation;
import org.htmlcleaner.XPatherException;

/**
 * Utility class to simplify the HTML produced by FitNesse.
 * 
 * @author smartrics
 */
public class HtmlSimplifier {
	private HtmlCleaner cleaner;
	private CleanerProperties props;
	private PrettyHtmlSerializer serializer;

	/**
	 * Default ctor.
	 */
	public HtmlSimplifier() {
		cleaner = new HtmlCleaner();
		setProperties();
		createSerializer();
		cleaner.setTransformations(createTransformations());
	}

	/**
	 * Simplifies an html block; adds title as in between <code>&lt;h1></code>
	 * tag.
	 * 
	 * @param title
	 *            the title.
	 * @param html
	 *            the html to simplify
	 * @return the simplified html.
	 */
	public String simplify(String title, String html) {
		TagNode tagNode = runCleanerAndAddTitle(title, html);
		tagNode = removeNodesMatching("//div[@class='meta']", tagNode);
		tagNode = tagNode.findElementByName("body", false);
		String result = serializeAndSanitiseResult(tagNode);
		return result;
	}

	private String serializeAndSanitiseResult(TagNode tagNode) {
		try {
			String result = serializer.getAsString(tagNode, true);
			result = result.replaceAll("&#160;", "&nbsp;");
			result = result.replaceAll("<br />\n\n<br />\n\n", "<br />\n\n");
			result = result.replaceAll("<br />\n\n<br />\n\n", "<br />\n\n");
			result = result.replaceAll("<br />\n\n", "<br />\n");
			result = result.replaceAll("<br />\n<br />\n", "<br />\n");
			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private TagNode removeNodesMatching(String xpath, TagNode tagNode) {
		try {
			Object[] resNodes = tagNode.evaluateXPath(xpath);
			for (Object n : resNodes) {
				if (n instanceof TagNode) {
					((TagNode) n).removeFromTree();
				}
			}
		} catch (XPatherException e1) {
			// ignore
		}
		return tagNode;
	}

	private TagNode runCleanerAndAddTitle(String title, String html) {
		TagNode tagNode = cleaner.clean("<h1>" + title + "</h1>\n" + html);
		return tagNode;
	}

	private void createSerializer() {
		serializer = new PrettyHtmlSerializer(props);
	}

	private CleanerTransformations createTransformations() {
		CleanerTransformations transformations = new CleanerTransformations();
		TagTransformation removeAnchors = new TagTransformation("a");
		transformations.addTransformation(removeAnchors);		
		TagTransformation removeImages = new TagTransformation("img");
		transformations.addTransformation(removeImages);	
		return transformations;
	}

	private void setProperties() {
		props = cleaner.getProperties();
		props.setOmitXmlDeclaration(true);
		props.setUseEmptyElementTags(false);
		props.setOmitComments(true);
		props.setTransSpecialEntitiesToNCR(true);
	}

}
