package smartrics.rest.fitnesse.fixture;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import fitnesse.junit.JUnitHelper;

//@Ignore
public class InProcessRestFixtureLiveDocTest {

	private JUnitHelper helper;

	@Before
	public void prepare() {
		helper = new JUnitHelper("src/main/resources", new File("target", "fitnesse-junit").getAbsolutePath());
		helper.setPort(9090);
	}

	@Test
	public void restFixtureTests() throws Exception {
		helper.assertSuitePasses("RestFixtureTests.LetTestsV2");
	}
}
