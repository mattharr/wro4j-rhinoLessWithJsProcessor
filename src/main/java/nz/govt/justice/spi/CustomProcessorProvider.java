package nz.govt.justice.spi;
import java.util.HashMap;
import java.util.Map;

import nz.govt.justice.processor.RhinoLessWithJsProcessor;

import ro.isdc.wro.model.resource.processor.ResourcePostProcessor;
import ro.isdc.wro.model.resource.processor.ResourcePreProcessor;
import ro.isdc.wro.model.resource.processor.support.ProcessorProvider;


/**
* Registers custom processors using SPI provider.
*
*/
public class CustomProcessorProvider
    implements ProcessorProvider {
  @Override
  public Map<String, ResourcePreProcessor> providePreProcessors() {
    final Map<String, ResourcePreProcessor> map = new HashMap<String, ResourcePreProcessor>();
    map.put(RhinoLessWithJsProcessor.ALIAS, new RhinoLessWithJsProcessor());
    return map;
  }

  @Override
  public Map<String, ResourcePostProcessor> providePostProcessors() {
    final Map<String, ResourcePostProcessor> map = new HashMap<String, ResourcePostProcessor>();
    map.put(RhinoLessWithJsProcessor.ALIAS, new RhinoLessWithJsProcessor());
    return map;
  }
}