/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.commit.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

public class PasteEditText extends EditText
{
	public PasteEditText(Context context)
	{
		super(context);
	}

	public PasteEditText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PasteEditText(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTextContextMenuItem(int id){
		return super.onTextContextMenuItem(id);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start,int lengthBefore, int lengthAfter)
	{
		if (!TextUtils.isEmpty(text) ){
			setText("");
		}
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
	}

}
