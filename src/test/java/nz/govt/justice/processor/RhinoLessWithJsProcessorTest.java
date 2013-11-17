package nz.govt.justice.processor;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

public class RhinoLessWithJsProcessorTest {

	@Test
	public void findJsIncludes_shouldReturnEmptyListForNoContent() {
		RhinoLessWithJsProcessor toTest = new RhinoLessWithJsProcessor();

		List<String> result = toTest.findJsIncludes("");

		assertThat(result.size(), is(0));
	}

	@Test
	public void findJsIncludes_shouldReturnSingleEntry() {
		RhinoLessWithJsProcessor toTest = new RhinoLessWithJsProcessor();

		List<String> result = toTest
				.findJsIncludes("//LESS //{{wro4jJsInclude:/js/colour_utils.js}} //Base Colours - Used to derive tints@navy:   #263e78;@blue:   #92abe8;@aqua:   #09afc1;@green:  #53c111;@yellow: #fdbb22;@red:    #d11830;@purple: #5344a6;@grey:   #888888;@white:  #ffffff;@black:  #000000;@light-blue: #adc1ef;@decision-text-green:#3E916B;");

		assertThat(result.size(), is(1));
		assertThat(result, hasItem("/js/colour_utils.js"));
	}

	@Test
	public void findJsIncludes_shouldReturnSeveralEntries() {
		RhinoLessWithJsProcessor toTest = new RhinoLessWithJsProcessor();

		List<String> result = toTest
				.findJsIncludes("//LESS //{{wro4jJsInclude:/js/colour_utils.js}} //Base Colours - Used to derive tints@navy:   #263e78;@blue:   #92abe8;@aqua:   #09afc1;@green:  #53c111;@yellow: #fdbb22;@red:    #d11830;@purple: #5344a6;@grey:   #888888;@white:  #ffffff;@black:  #000000;@light-blue: #adc1ef;@decision-text-green:#3E916B;//{{wro4jJsInclude:test.js}}");

		assertThat(result.size(), is(2));
		assertThat(result, hasItem("/js/colour_utils.js"));
		assertThat(result, hasItem("test.js"));
	}

}
