# Setting up Eclipse

To develop your application via Eclipse, you need to install the following pieces of software.

Java Development Kit 7+ (JDK) (6 will not work!)

Eclipse, the "Eclipse IDE for Java Developers" is usually sufficient.

Android SDK, you only need the SDK (available at the bottom of the page in the 'command line tools' section), not the whole Android Studio package which is a customized version of Intellij bundled with the Android SDK. Install the latest stable platform via the SDK Manager.

Android Development Tools for Eclipse, aka ADT Plugin. Use this update site: https://dl-ssl.google.com/android/eclipse/

Eclipse Integration Gradle, use this update site: http://dist.springsource.com/snapshot/TOOLS/gradle/nightly (for Eclipse 4.4) or http://dist.springsource.com/release/TOOLS/gradle (for Eclipse < 4.4)

# Local Configurations

Add file local.properties with the text:

	# Location of the android SDK
	sdk.dir=C:/Users/ecurri3/...
Important to use '/' and not '\'

Import into Eclipse

	File -> Import -> Gradle Project
	Navigate to cloned repository

	When asked if you want to overwrite or keep project structure, select 'Overwrite'

Once imported, right-click android folder in project explorer and select Properties -> Android and make sure Project Build Target has one sdk selected. Click Apply and OK.

Navigate to Run -> Run Configurations -> Choose DesktopLauncher -> Arguments tab -> Working Directory -> select Other and navigate to the android/assets directory. Click Apply and Run.
