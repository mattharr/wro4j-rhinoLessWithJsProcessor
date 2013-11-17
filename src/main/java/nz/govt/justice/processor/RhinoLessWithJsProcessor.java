package nz.govt.justice.processor;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nz.govt.justice.support.LessCssCustom;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.isdc.wro.WroRuntimeException;
import ro.isdc.wro.config.ReadOnlyContext;
import ro.isdc.wro.extensions.processor.css.RhinoLessCssProcessor;
import ro.isdc.wro.extensions.processor.support.ObjectPoolHelper;
import ro.isdc.wro.extensions.processor.support.less.LessCss;
import ro.isdc.wro.model.group.Inject;
import ro.isdc.wro.model.resource.Resource;
import ro.isdc.wro.model.resource.ResourceType;
import ro.isdc.wro.model.resource.SupportedResourceType;
import ro.isdc.wro.model.resource.locator.factory.UriLocatorFactory;
import ro.isdc.wro.model.resource.processor.ResourcePostProcessor;
import ro.isdc.wro.model.resource.processor.ResourcePreProcessor;
import ro.isdc.wro.util.ObjectFactory;

@SupportedResourceType(ResourceType.CSS)
public class RhinoLessWithJsProcessor implements ResourcePreProcessor, ResourcePostProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(RhinoLessCssProcessor.class);
	public static final String ALIAS = "rhinoLessCssWithJs";
	private ObjectPoolHelper<LessCssCustom> enginePool;

	@Inject
	private ReadOnlyContext context;

	@Inject
	private UriLocatorFactory uriLocatorFactory;

	public RhinoLessWithJsProcessor() {
		enginePool = new ObjectPoolHelper<LessCssCustom>(new ObjectFactory<LessCssCustom>() {
			@Override
			public LessCssCustom create() {
				return newLessCss();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(final Resource resource, final Reader reader, final Writer writer) throws IOException {
		final String content = IOUtils.toString(reader);
		List<String> jsIncludes = findJsIncludes(content);

		final LessCssCustom lessCss = enginePool.getObject();

		try {
			writer.write(lessCss.less(content, uriLocatorFactory, jsIncludes));
		} catch (final WroRuntimeException e) {
			final String resourceUri = resource == null ? StringUtils.EMPTY : "[" + resource.getUri() + "]";
			LOG.warn("Exception while applying " + getClass().getSimpleName() + " processor on the " + resourceUri
					+ " resource, no processing applied...", e);
			onException(e);
		} finally {
			// return for later reuse
			enginePool.returnObject(lessCss);
			reader.close();
			writer.close();
		}
	}

	// //{{wro4jJsInclude:/js/colour_utils.js}}
	List<String> findJsIncludes(String content) {
		Pattern p = Pattern.compile("//\\{\\{wro4jJsInclude:(.*?)\\}\\}");
		Matcher matcher = p.matcher(content);
		List<String> returnValue = new ArrayList<>();
		while (matcher.find()) {
			returnValue.add(matcher.group(1));
		}
		return returnValue;
	}

	/**
	 * Invoked when a processing exception occurs.
	 */
	protected void onException(final WroRuntimeException e) {
		throw e;
	}

	/**
	 * @return the {@link LessCss} engine implementation. Override it to provide a different version of the less.js
	 *         library. Useful for upgrading the processor outside the wro4j release.
	 */
	protected LessCssCustom newLessCss() {
		return new LessCssCustom();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(final Reader reader, final Writer writer) throws IOException {
		process(null, reader, writer);
	}

	// @Override
	// public void destroy() throws Exception {
	// enginePool.destroy();
	// }

}
