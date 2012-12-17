package smartrics.rest.support.fitnesse;

import fitnesse.responders.ResponderFactory;
import fitnesse.responders.run.SuiteResponder;

/**
 * A responder that handles format=markdown by supplying a
 * {@link SimpleHtmlFormatter} to generate a result page in markdown format.
 * 
 * This responder must be configured in fitnesse via plugin extensions (see
 * http://fitnesse.org/FitNesse.UserGuide.PluginUsage)
 * 
 * Note that the list of available responders is in {@link ResponderFactory}.
 * 
 * Run it with: <code>/RestFixtureTests?suite&responder=suite&format=simplehtml&output=target/simple.html</code>
 * 
 * @author smartrics
 */
public class SuiteWithMarkdownResponder extends SuiteResponder {

	@Override
	protected void createFormatterAndWriteHead() throws Exception {
		super.createFormatterAndWriteHead();
		if (request.hasInput("format")) {
			String format = request.getInput("format").toString();
			if("simplehtml".equals(format)) {
				String output = "simplified_output.html";
				if(request.hasInput("output")) {
					output = request.getInput("output").toString();
				}
				formatters.add(new SimpleHtmlFormatter(output));
			}
		}
	}


}
