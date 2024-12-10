/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.proit.wicket.forms;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;

public class CaptchaForm<T> extends Form<T> {
    private static final long serialVersionUID = 1L;
    
    private WebMarkupContainer captchaContainer;

    private CaptchaImageResource captchaImageResource;
    
    private TextField<String> captchaTextField;

    public CaptchaForm(String id) {
        super(id);
        
        captchaContainer = new WebMarkupContainer("captchaContainer");
        captchaContainer.setOutputMarkupPlaceholderTag(true);
        add(captchaContainer);
        
        captchaImageResource = new CaptchaImageResource();
        final Image captchaImage = new Image("image", captchaImageResource);
        captchaImage.setOutputMarkupId(true);
        captchaContainer.add(captchaImage);

        AjaxLink<Void> changeCaptchaLink = new AjaxLink<Void>("changeLink") {
			private static final long serialVersionUID = 1L;

			@Override
            public void onClick(AjaxRequestTarget target) {
                captchaImageResource.invalidate();
                target.add(captchaImage);
            }
        };
        captchaContainer.add(changeCaptchaLink);

        captchaTextField = new TextField<String>("captchaText", Model.of("")) {
			private static final long serialVersionUID = 1L;

			@Override
            protected final void onComponentTag(final ComponentTag tag) {
                super.onComponentTag(tag);
                // clear the field after each render
                tag.put("value", "");
            }
        };
        captchaContainer.add(captchaTextField);
    }
    
	/**
	 * Valida el texto del captcha ingresado por pantalla.
	 */
    protected void verifyCaptcha() {
		if ( captchaTextField.getConvertedInput() == null) {
			error("Debe ingresar el texto del captcha.");
		} else if ( !captchaImageResource.getChallengeId().equals(captchaTextField.getConvertedInput())) {
			error("El texto del captcha ingresado no es el correcto. Por favor, vuelva a intentarlo.");
        }
	}
    
}