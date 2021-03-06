/*
 * RHQ Management Platform
 * Copyright (C) 2005-2010 Red Hat, Inc.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.rhq.coregui.client.inventory.resource.selection;

import java.util.Set;

import com.google.gwt.user.client.Timer;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

import org.rhq.coregui.client.CoreGUI;
import org.rhq.coregui.client.Messages;
import org.rhq.coregui.client.util.enhanced.EnhancedVLayout;

/**
 * A window dialog box that lets you pick one or more resources.
 * 
 * @author John Mazzitelli
 */
public class ResourcePicker extends Window {

    protected Messages MSG = CoreGUI.getMessages();

    private final OkHandler okHandler;
    private final CancelHandler cancelHandler;

    private ResourceSelector selector;
    private Label warningLabel;

    public ResourcePicker(OkHandler okHandler, CancelHandler cancelHandler) {
        super();

        if (okHandler == null) {
            throw new IllegalArgumentException("okHandler == null");
        }

        this.okHandler = okHandler;
        this.cancelHandler = cancelHandler;

        if (getTitle() == null || getTitle().contains("Untitled")) {
            setTitle(getDefaultTitle());
        }

        setShowModalMask(true);
        setShowMinimizeButton(false);
        setIsModal(true);
        setWidth(700);
        setHeight(425);
        setAutoCenter(true);
        centerInPage();

        addCloseClickHandler(new CloseClickHandler() {
            @Override
            public void onCloseClick(CloseClickEvent event) {
                cancel();
            }
        });

        EnhancedVLayout layout = new EnhancedVLayout();
        layout.setLayoutAlign(Alignment.CENTER);
        layout.setLayoutMargin(10);

        warningLabel = new Label();
        warningLabel.setContents("");
        warningLabel.setAlign(Alignment.CENTER);
        warningLabel.setStyleName("WarnBlock");
        warningLabel.setAutoHeight();
        warningLabel.setWidth100();
        warningLabel.setWrap(false);
        warningLabel.setVisible(false);

        selector = getResourceSelector();

        DynamicForm form = new DynamicForm();
        form.setLayoutAlign(Alignment.CENTER);
        form.setAutoWidth();
        form.setAutoHeight();
        form.setCellPadding(10);

        ButtonItem ok = new ButtonItem("ok", MSG.common_button_ok());
        ok.setStartRow(true);
        ok.setEndRow(false);
        ok.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ok();
            }
        });
        ButtonItem cancel = new ButtonItem("cancel", MSG.common_button_cancel());
        cancel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                cancel();
            }
        });
        cancel.setStartRow(false);
        cancel.setEndRow(true);
        form.setFields(ok, cancel);

        layout.addMember(warningLabel);
        layout.addMember(selector);
        layout.addMember(form);
        addItem(layout);
    }

    protected OkHandler getOkHandler() {
        return okHandler;
    }

    protected CancelHandler getCancelHandler() {
        return cancelHandler;
    }

    protected String getDefaultTitle() {
        return MSG.widget_resourceSelector_selectMultipleResources();
    }

    protected ResourceSelector getResourceSelector() {
        if (selector == null) {
            selector = createResourceSelector();
        }
        return selector;
    }

    protected ResourceSelector createResourceSelector() {
        return new ResourceSelector();
    }

    protected void ok() {
        OkHandler handler = getOkHandler();
        Set<Integer> selection = selector.getSelection();

        if (selection == null || selection.isEmpty()) {
            showWarningMessage(MSG.widget_resourceSelector_pleaseSelectMultipleResource());
        } else {
            if (handler.ok(selection)) {
                selector = null;
                markForDestroy();
            }
        }
    }

    protected void cancel() {
        CancelHandler handler = getCancelHandler();
        if (handler != null) {
            handler.cancel();
        }
        selector = null;
        markForDestroy();
    }

    protected void showWarningMessage(String msg) {
        warningLabel.setContents(msg);
        warningLabel.show();
        markForRedraw();

        // just show the warning message for a short time, hide it after a few seconds
        Timer timer = new Timer() {
            @Override
            public void run() {
                warningLabel.hide();
                markForRedraw();
            }
        };
        timer.schedule(15000);
    }

    /**
     * The handler type used to be notified when the ok button is pressed.
     * If false is returned by the ok method, the picker window will not
     * close - true means the picker can close the window.
     */
    public interface OkHandler {
        public boolean ok(Set<Integer> resourceIdSelection);
    }

    /**
     * The handler type used to be notified when the cancel button is pressed
     * or when the close-button in the title bar is pressed.
     * The picker window will always be closed when canceled.
     */
    public interface CancelHandler {
        public void cancel();
    }
}
