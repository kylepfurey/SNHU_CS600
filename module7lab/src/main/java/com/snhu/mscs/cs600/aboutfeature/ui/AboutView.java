package com.snhu.mscs.cs600.aboutfeature.ui;

/**
 * Imports that support the Vaadin flow UI.
 */
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.client.RestTemplate;

/**
 * The toolbar we have created for this application
 */
import com.snhu.mscs.cs600.base.ui.component.ViewToolbar;

/**
 * Annotation that allows this view to be used without being authenticated.
 */
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * Imports to work with JSON result sets.
 */
import org.springframework.boot.json.JsonParser;
import java.util.Map;

/**
 * AboutView - 
 * 
 * Purpose:
 *      The purpose of this class is to provide the UI view for a simple
 *      page that describes the purpose of this application. This is a very
 *      basic implementation of a view that has a single component 
 *      (an HTML Div object) styled to contain HTML formatted text.
 * 
 * Extends: 
 *      VeritcalLayout - A basic layout structure where items are added to
 *      the canvas in a vertical orientation.
 *
 * Class Level Annotations:
 *      @Route - This tells the Vaadin framework what the URL path is 
 *      relative to our base URL. "" is the default route for the 
 *      application.
 *      @AnonymousAllowed - This tells Vaadin and Spring Security that 
 *      this page can be displayed without the user having logged in.
 *      @Menu - This indicates to the Vaadin framework that the view 
 *      should be represented in any menu component for the application
 *      and provides the parameters to order, display, and name the 
 *      menu item.
 * 
 * Sample Output: N/A
 * 
 * Constraints: N/A
 * 
 * Expected Results: 
 *      This class will be used as a landing page for the UI.
 * 
 * Version              Author      Date            Description
 * ***********************************************************************
 *     1                 fjm        20-Oct-2025     Initial Creation
 */
@Route("")
@AnonymousAllowed
@Menu(order = 0, icon = "vaadin:clipboard-check", title = "About")
public class AboutView extends VerticalLayout {

    /**
     * AboutView - As this is a very simple page for our application to display
     *             as a default, all of the configuration is handled in the 
     *             default constructor.
     */
    public AboutView() {
    
        StringBuilder text = new StringBuilder();
        text.append("<P>This is an example of a UI built with the Vaadin Framework.</P>\n");
        text.append("<P>This builds on the Lab from Module 3, utilizing the same concept ");
        text.append("of a list of names stored with Spring JPA in a PostgreSQL database.</P>");
        text.append("<P>Additionally this carries forward the UI that was added in module 5, ");
        text.append("and here the activity will add integration with an external API.</P>");
        text.append("<BR/><B><U>Notice:</B></U> In order to see or select the Names View, ");
        text.append("you must authenticate.</P>\n");

        Div content = new Div();
        content.getStyle().set("display", "block").set("white-space", "pre-wrap");
        content.getElement().setProperty("innerHTML", text.toString());

        // Setup sunrise and sunset
        String[] ss = this.getSunriseAndSet();

        // Style similar to page title
        var v0 = new H1("Location: Nauset Light, Cape Cod MA");
        var v1 = new H1(ss[0]);
        var v2 = new H1(ss[1]);
        var v3 = new H1(ss[2]);
        var v4 = new H1(ss[3]);
        v0.addClassNames(FontSize.XLARGE, Margin.NONE, FontWeight.LIGHT);
        v1.addClassNames(FontSize.XLARGE, Margin.NONE, FontWeight.LIGHT);
        v2.addClassNames(FontSize.XLARGE, Margin.NONE, FontWeight.LIGHT);
        v3.addClassNames(FontSize.XLARGE, Margin.NONE, FontWeight.LIGHT);
        v4.addClassNames(FontSize.XLARGE, Margin.NONE, FontWeight.LIGHT);
        
        // Add toolbar
        add(new ViewToolbar("About", v0, v1, v2, v3, v4));
        add(content);
    }

    /**
     * getSunriseAndSunset - 
     * 
     * Call sunrise/sunset API from  https://api.sunrisesunset.io/
     * 
     * Making a call to this API requires you to know the latitude and
     * longitude for the location you are inquiring about. In this case, 
     * 
     * lat = 41.86308, lng=-69.94331
     * 
     * Is the location of Nauset Lighthouse in Cape Cod Massachusetts.
     * You can replace this with a place of your choosing, just 
     * update the information below accordingly.
     * 
     * The full API call is:
     * 
     * https://api.sunrisesunset.io/json?lat=41.86308&lng=-69.94331
     * 
     * @returns String[] containing sunrise and sunset information and labels
     */
    @SuppressWarnings("unchecked")
    private String[] getSunriseAndSet()
    {
        // Setup Template to make API call
        RestTemplate rt = new RestTemplate();
        String uri = new String("https://api.sunrisesunset.io/json?lat=41.86308&lng=-69.94331");

        // Call external API
        String result = rt.getForObject(uri, String.class);

        /*
         * Our result data should look something like this:
         * 
         * {"results":{"date":"2025-10-29","sunrise":"7:08:38 AM","sunset":"5:40:51 PM",
         * "first_light":"5:34:53 AM","last_light":"7:14:35 PM","dawn":"6:39:56 AM",
         * "dusk":"6:09:33 PM","solar_noon":"12:24:44 PM","golden_hour":"5:01:40 PM",
         * "day_length":"10:32:12","timezone":"America/New_York","utc_offset":-240},"status":"OK"}
         * 
         * Note: This can be viewed with a Map that contains a nested Map.
         */

        // Parse JSON Result into a Map
        JsonParser jsParse = JsonParserFactory.getJsonParser();
        Map < String, Object > map = jsParse.parseMap(result);
        
        // Grab the section of the results object that contains our data (nested Map)
        Map < String, Object >  m1 = (Map<String,Object>)map.get("results");

        // Parse Sunrise and Sunset
        String sunrise = new String((String)m1.get("sunrise"));
        String rise = new String("Sunrise: ");
        String sunset = new String((String)m1.get("sunset"));
        String set = new String("Sunset: ");

        // Return dataset
        return new String[] {rise, sunrise, set, sunset};
    }
}

