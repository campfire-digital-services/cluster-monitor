package au.com.permeance.clusterMonitor.controller;

import com.liferay.portal.model.Portlet;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.ControlPanelEntry;

public class ControlPanelEntryImpl implements ControlPanelEntry {

    @Override
    public boolean isVisible(final PermissionChecker permissionChecker,
                             final Portlet portlet) {

        return permissionChecker.isOmniadmin();
    }

    @Override
    public boolean isVisible(final Portlet portlet,
                             final String category,
                             final ThemeDisplay themeDisplay) throws Exception {

        final PermissionChecker permissionChecker = themeDisplay.getPermissionChecker();

        return isVisible(permissionChecker, portlet);
    }

}
