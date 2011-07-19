class PluginTestFunctionalTests extends functionaltestplugin.FunctionalTestCase {


	void testGobaby() {
        get '/pluginTest/goBaby'
        assertStatus 200
        assertContentContains 'fly away'
		assertContentContains 'This is a ftl from a plugin'
    }

	void testBluesky() {
        get '/pluginTest/bluesky'
        assertStatus 200
        assertContentContains 'fly away'
		assertContentContains 'Blue Skies'
    }

	void testItWorks() {
        get '/pluginTest/itWorks'
        assertStatus 200
        assertContentContains 'you know it'
    }

	void testOverride() {
        get '/pluginTest/override'
        assertStatus 200
        assertContentContains 'this is the one you should see'
    }

}

