package org.example.studinfo.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("")
public class HomeView extends AppLayout {
    SideNav nav = new SideNav();

    public HomeView() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("StudInfo");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        SideNavItem students = new SideNavItem("Students", StudentsView.class);
        SideNavItem groups = new SideNavItem("Groups", GroupsView.class);
        nav.addItem(students, groups);

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
        addToNavbar(toggle, title);

        setPrimarySection(Section.DRAWER);
    }
}
