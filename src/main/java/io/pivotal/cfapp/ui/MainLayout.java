package io.pivotal.cfapp.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

import io.pivotal.cfapp.ui.view.HomeView;
import io.pivotal.cfapp.ui.view.SnapshotApplicationDetailView;
import io.pivotal.cfapp.ui.view.SnapshotServiceInstanceDetailView;


public class MainLayout extends AppLayout {

    private static final long serialVersionUID = 1L;

    public MainLayout() {
    	Tab homeTab = createTab(VaadinIcon.HOME.create(), "Home", HomeView.class);

    	Accordion accordion = new Accordion();

    	Tabs snapshotDetailTabs = createTabs();
    	Tab sadTab = createTab(VaadinIcon.TABLE.create(), "Application", SnapshotApplicationDetailView.class);
    	Tab sidTab = createTab(VaadinIcon.TABLE.create(), "Service Instance", SnapshotServiceInstanceDetailView.class);

    	snapshotDetailTabs.add(sadTab, sidTab);
    	accordion.add("Snapshot Detail", snapshotDetailTabs).addThemeVariants(DetailsVariant.REVERSE);

    	addToNavbar(true, homeTab, new DrawerToggle());
    	addToDrawer(accordion);
    }

    private Tabs createTabs() {
    	Tabs menu = new Tabs();
    	menu.setWidthFull();
    	menu.setOrientation(Tabs.Orientation.VERTICAL);
    	menu.setFlexGrowForEnclosedTabs(1);
    	return menu;
    }

    private Tab createTab(Icon icon, String label, Class<? extends Component> layout) {
    	RouterLink link = new RouterLink(label, layout);
    	Tab tab = new Tab();
    	tab.add(icon, link);
    	return tab;
    }

}