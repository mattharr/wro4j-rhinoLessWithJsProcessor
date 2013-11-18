wro4j-rhinoLessWithJsProcessor
==============================

This processor is based on the RhinoLessProcessor, but adds the functionality to include Javascript files into the Rhino processor, so that Javascript functions can be used within the less file.

As the processor works through the less files it looks for the following special comment in the css file:

//{{wro4jJsInclude:[path]}}

Where [path] is the path to the file relative to the contextFolder, for example:

//{{wro4jJsInclude:/js/colour_utils.js}}

When it finds a comment of this form it will look to add the specified javascript file to Rhino prior to compiling the less file.
