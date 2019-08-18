package com.github.cbuschka.retour.retour_processor;

import com.bertramlabs.plugins.hcl4j.HCLParser;
import com.bertramlabs.plugins.hcl4j.HCLParserException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RetourProcessorTfTest {

	private HCLParser hclParser = new HCLParser();

	private Map tfConfig;

	@Test
	public void lambdaHandlerClassCorrect() throws IOException, HCLParserException {

		File tfFile = givenIsATerraformLambdaConfig();

		whenParsed(tfFile);

		thenHandlerClassNameIsOf(RetourLambdaHandler.class);
	}

	private void thenHandlerClassNameIsOf(Class<RetourLambdaHandler> clazz) {
		Map resource = (Map) tfConfig.get("resource");
		Map awsLambdaFunction = (Map) resource.get("aws_lambda_function");
		Map retourProcessorFunction = (Map) awsLambdaFunction.get("retour_processor_function");
		String handlerClassName = (String) retourProcessorFunction.get("handler");

		assertThat(handlerClassName, is(clazz.getName()));
	}

	private void whenParsed(File tfFile) throws HCLParserException, IOException {
		this.tfConfig = hclParser.parse(tfFile, "UTF-8");
	}

	private File givenIsATerraformLambdaConfig() {
		return new File("retour-processor.tf");
	}

}