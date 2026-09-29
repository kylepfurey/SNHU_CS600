package com.snhu.mscs.cs600.base.ui.component;

/**
 * Imports for Vaadin UI components
 */
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * ViewToolbar - 
 * 
 * Purpose:
 *      The purpose of this class is to create and provide a Toolbar
 *      component that is utilized throughout our UI.
 * 
 * Extends: 
 *      Composite<Header> - Class that allows you to compose more than one
 *      component into a component group.
 *
 * Class Level Annotations:
 *      @NullMarked - An annotation that indicates portions of this class are 
 *      Nullable. This reduces the need to deploy many @Nullable annotations
 *      for individual parts of the class.
 * 
 * Sample Output: N/A
 * 
 * Constraints: N/A
 * 
 * Expected Results: 
 *      This class provides a Toolbar as a reusable UI component.
 * 
 * Version              Author      Date            Description
 * ***********************************************************************
 *     1                 fjm        20-Oct-2025     Initial Creation
 */
@NullMarked
public final class ViewToolbar extends Composite<Header> {

    /**
     * ViewToolbar - Class that is designed to create a reusable UI object to be used
     * as a toolbar throughout our UI. This will aggregate one or more components
     * and agroup them so that they can be presented and manipulated as a group.
     * 
     * @param viewTitle String representing the title of the View.
     * @param components One or more components to be grouped into the toolbar.
     */
    public ViewToolbar(@Nullable String viewTitle, Component... components) {
        // Assign UI parameters.
        addClassNames(Display.FLEX, FlexDirection.COLUMN, JustifyContent.BETWEEN, AlignItems.STRETCH, Gap.MEDIUM,
                FlexDirection.Breakpoint.Medium.ROW, AlignItems.Breakpoint.Medium.CENTER);

        // Create a toolbar toggle to hide the sidebar.
        var drawerToggle = new DrawerToggle();
        drawerToggle.addClassNames(Margin.NONE);

        // Title foro the view
        var title = new H1(viewTitle);
        title.addClassNames(FontSize.XLARGE, Margin.NONE, FontWeight.LIGHT);

        // Combine everything into an HTML Div construct
        var toggleAndTitle = new Div(drawerToggle, title);
        toggleAndTitle.addClassNames(Display.FLEX, AlignItems.CENTER);
        getContent().add(toggleAndTitle);

        // Style the components
        if (components.length > 0) {
            var actions = new Div(components);
            actions.addClassNames(Display.FLEX, FlexDirection.COLUMN, JustifyContent.BETWEEN, Flex.GROW, Gap.SMALL,
                    FlexDirection.Breakpoint.Medium.ROW);
            getContent().add(actions);
        }
    }

    /**
     * group - Helper method used to group one or more components into a single HTML
     * Div construct that will be treated as a single component.
     * 
     * @param components one or more Components to group into a single container (Component)
     * @return Component representing an HTML Div containing all of the Components from the method's
     *         parameters.
     */
    public static Component group(Component... components) {
        var group = new Div(components);
        group.addClassNames(Display.FLEX, FlexDirection.COLUMN, AlignItems.STRETCH, Gap.SMALL,
                FlexDirection.Breakpoint.Medium.ROW, AlignItems.Breakpoint.Medium.CENTER);
        return group;
    }
}
