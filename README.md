# MyWeatherApp

### Search for Weather Information based on city, zip code, or current location
* defaults to St. Louis, MO

### About
- Android Studio Version: 2020.3.1 Patch 3
- CompileSdkVersion: 31
- Build Tools Version: 30.0.2
- Min Sdk Version: 29
- Target Sdk Version 31
- Tested Mainly With: Pixel 4 API 30 emulator (Must have emulator with play store for location services)

### Future Work (with more time):
<ul>
  <li>Learn more about Dagger</li>
  <ul>
    <li>Hook up Connectivity Interceptor to handle crash when no access to Internet</li>
  </ul>
  <li>Double check handling of failed cases: (no location services, no Internet, bad request, etc.)</li>
  <li>Condense Weather Response into a WeatherModel with only data used</li>
  <li>Add Room Database</li>
  <ul>
    <li>Store default data (last searched)</li>
    <li>Store settings information (below)</li>
    <li>Store recently searched locations</li>
  </ul>
  <li>UI</li>
  <ul>
    <li>Better design with Material Design components added in</li>
    <li>Color Scheme</li>
    <li>Show recently searched or saved locations (new fragment, maybe tab)</li>
    <li>Make Error Toasts more visible</li>
    <li>Add Settings Menu (ellipses)</li>
    <ul>
      <li>Dark Mode Toggle</li>
      <li>Imperial vs Metric</li>
      <li>Choose Default (last searched or current location)</li>
    </ul>
  </ul>
</ul>
