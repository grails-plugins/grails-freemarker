/*
	This is the Geb configuration file.
	
	See: http://www.gebish.org/manual/current/configuration.html
*/


import io.github.bonigarcia.wdm.ChromeDriverManager
import io.github.bonigarcia.wdm.InternetExplorerDriverManager
import io.github.bonigarcia.wdm.PhantomJsDriverManager

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver

// http://www.gebish.org/manual/current/configuration.html#waiting_for_base_navigator
baseNavigatorWaiting = true

atCheckWaiting = true
baseUrl =  'http://localhost:8080/'

//****Chrome as the default driver *****//
ChromeDriverManager.getInstance().setup()
driver = { new ChromeDriver() }

environments {

  firefox {
    // See: https://github.com/SeleniumHQ/selenium/wiki/FirefoxDriver
    driver = { new FirefoxDriver() }
  }

  // run as "grails -Dgeb.env=chrome test-app functional:"
  // See: https://sites.google.com/a/chromium.org/chromedriver/
  chrome {
    // Download and configure ChromeDriver using https://github.com/bonigarcia/webdrivermanager
    ChromeDriverManager.getInstance().setup()

    driver = { new ChromeDriver() }
  }

  // run as "grails -Dgeb.env=ie test-app functional:"
  // See: https://github.com/SeleniumHQ/selenium/wiki/InternetExplorerDriver
  ie {
    // Download and configure InternetExplorerDriver using https://github.com/bonigarcia/webdrivermanager
    InternetExplorerDriverManager.getInstance().setup()

    driver = { new InternetExplorerDriver() }
  }

  phantom {
    PhantomJsDriverManager.getInstance().setup()
    waiting {
      timeout = 2
    }
    driver = { new PhantomJSDriver() }
  }
  
}
