# box-java-sdk-v2-metadata

## Important Notice
Box's metadata functionality is in beta and is subject to frequent change.
Do not use it with production content as the data will be frequently purged.
By using the beta, you are acknowledging that you have read and agreed to our
[beta terms of service](https://cloud.box.com/s/w73uuums8jjaumtri853). If you
have questions, send an email to metadata-beta@box.com.

## Overview

This is a standard java libarary, it is a plugin for [box-java-sdk-v2](https://github.com/box/box-java-sdk-v2) or [box-android-sdk-v2](https://github.com/box/box-android-sdk-v2)
that enables beta support for Box's metadata API. In order to use this plugin, you need to also add either of the sdks as reference library. 
Please refer to [box-java-sdk-v2 README](https://github.com/box/box-java-sdk-v2) or [box-android-sdk-v2 README](https://github.com/box/box-android-sdk-v2) for sdk usage.
Also see
[metadata API documentation](https://developers.box.com/metadata-api/) and
[product documentation](https://developers.box.com/metadata-web-application/).

**Note**: This library and the HTTP API it wraps are in beta and may undergo breaking
changes.

### Usage


Use [box-java-sdk-v2](https://github.com/box/box-java-sdk-v2) or [box-android-sdk-v2](https://github.com/box/box-android-sdk-v2) as you normally would. Whenever you wish to use the metadata API,

Plugin metadata manager like this:

```java
	BoxFileMetadataPlugin plugin = new BoxFileMetadataPlugin();
        BoxFileMetadataManager manager = plugin.plugin(client);
```

Make metadata api call like this:
```java
	manager.updateMetadata(fileId, key1, value1);
```
The metadata manager can also be retrieved like this after you get it plugged in:
```java
	BoxFileMetadataManager manager = (BoxFileMetadataManager) client.getPluginManager(BoxFileMetadataPlugin.FILE_METADATA_MANAGER_KEY);
```

For more detailed usage, please also refer to HelloMetadata.java under com.box.boxmetadatalibv2.example package.

The metadata API is in beta, it has not been activated for all users.
**Note**: Box may change which classes of users are enabled to use the metadata feature.

## Copyright and License

Copyright 2014 Box, Inc. All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

