/* Copyright 2014 Braden Farmer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.farmerbb.notepad;

import android.app.*;
import android.os.*;
import android.view.*;

public class MainActivity extends Activity implements 
BackButtonDialogFragment.Listener, 
DeleteDialogFragment.Listener, 
SaveButtonDialogFragment.Listener,
NoteListFragment.Listener,
NoteEditFragment.Listener, 
NoteViewFragment.Listener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        // Begin a new FragmentTransaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // This fragment shows NoteListFragment as a sidebar (only seen in tablet mode landscape)
        transaction.replace(R.id.noteList, new NoteListFragment(), "NoteListFragment");

        // This fragment shows NoteListFragment in the main screen area (only seen on phones and tablet mode portrait),
        // but only if it doesn't already contain NoteViewFragment or NoteEditFragment
        if(!((getFragmentManager().findFragmentById(R.id.noteViewEdit) instanceof NoteEditFragment)
                || (getFragmentManager().findFragmentById(R.id.noteViewEdit) instanceof NoteViewFragment)))
            transaction.replace(R.id.noteViewEdit, new NoteListFragment(), "NoteListFragment");

        // Commit fragment transaction
        transaction.commit();
	}

	// Keyboard shortcuts	
	@Override
	public boolean dispatchKeyShortcutEvent(KeyEvent event) {
		super.dispatchKeyShortcutEvent(event);
		if(event.getAction() == KeyEvent.ACTION_DOWN &&
		   event.isCtrlPressed()) {		
		   
			if(getFragmentManager().findFragmentById(R.id.noteViewEdit) instanceof NoteListFragment) {
				NoteListFragment fragment = (NoteListFragment) getFragmentManager().findFragmentByTag("NoteListFragment");
				fragment.dispatchKeyShortcutEvent(event.getKeyCode());
			} else if(getFragmentManager().findFragmentById(R.id.noteViewEdit) instanceof NoteViewFragment) {
				NoteViewFragment fragment = (NoteViewFragment) getFragmentManager().findFragmentByTag("NoteViewFragment");
				fragment.dispatchKeyShortcutEvent(event.getKeyCode());
			} else if(getFragmentManager().findFragmentById(R.id.noteViewEdit) instanceof NoteEditFragment) {
				NoteEditFragment fragment = (NoteEditFragment) getFragmentManager().findFragmentByTag("NoteEditFragment");
				fragment.dispatchKeyShortcutEvent(event.getKeyCode());
			}

			return true;
		}
		return super.dispatchKeyShortcutEvent(event);
	}

	@Override
	public void onDeleteDialogPositiveClick() {
		if(getFragmentManager().findFragmentById(R.id.noteViewEdit) instanceof NoteViewFragment) {
			NoteViewFragment fragment = (NoteViewFragment) getFragmentManager().findFragmentByTag("NoteViewFragment");
			fragment.onDeleteDialogPositiveClick();
		} else if(getFragmentManager().findFragmentById(R.id.noteViewEdit) instanceof NoteEditFragment) {
			NoteEditFragment fragment = (NoteEditFragment) getFragmentManager().findFragmentByTag("NoteEditFragment");
			fragment.onDeleteDialogPositiveClick();
		}
	}

	@Override
	public void onBackPressed() {
		if(getFragmentManager().findFragmentById(R.id.noteViewEdit) instanceof NoteListFragment) {
			NoteListFragment fragment = (NoteListFragment) getFragmentManager().findFragmentByTag("NoteListFragment");
			fragment.onBackPressed();
		} else if(getFragmentManager().findFragmentById(R.id.noteViewEdit) instanceof NoteViewFragment) {
			NoteViewFragment fragment = (NoteViewFragment) getFragmentManager().findFragmentByTag("NoteViewFragment");
			fragment.onBackPressed();
		} else if(getFragmentManager().findFragmentById(R.id.noteViewEdit) instanceof NoteEditFragment) {
			NoteEditFragment fragment = (NoteEditFragment) getFragmentManager().findFragmentByTag("NoteEditFragment");
			fragment.onBackPressed();
		}
	}

    @Override
    // Method used by selecting a existing note from the ListView in NoteViewFragment
    // We need this method in MainActivity because sometimes getFragmentManager() is null
    public void viewNote(String filename) {
        Bundle bundle = new Bundle();
        bundle.putString("filename", filename);

        Fragment fragment = new NoteViewFragment();
        fragment.setArguments(bundle);

        // Add NoteViewFragment
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.noteViewEdit, fragment, "NoteViewFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

    }

	@Override
	public void onBackDialogNegativeClick() {
		NoteEditFragment fragment = (NoteEditFragment) getFragmentManager().findFragmentByTag("NoteEditFragment");
		fragment.onBackDialogNegativeClick();
	}

	@Override
	public void onBackDialogPositiveClick() {
		NoteEditFragment fragment = (NoteEditFragment) getFragmentManager().findFragmentByTag("NoteEditFragment");
		fragment.onBackDialogPositiveClick();
	}

	@Override
	public void onSaveDialogNegativeClick() {
		NoteEditFragment fragment = (NoteEditFragment) getFragmentManager().findFragmentByTag("NoteEditFragment");
		fragment.onSaveDialogNegativeClick();
	}

	@Override
	public void onSaveDialogPositiveClick() {
		NoteEditFragment fragment = (NoteEditFragment) getFragmentManager().findFragmentByTag("NoteEditFragment");
		fragment.onSaveDialogPositiveClick();
	}

	@Override
	public void showBackButtonDialog() {
		DialogFragment backFragment = new BackButtonDialogFragment();
		backFragment.show(getFragmentManager(), "back");
	}

	@Override
	public void showDeleteDialog() {
		DialogFragment deleteFragment = new DeleteDialogFragment();
		deleteFragment.show(getFragmentManager(), "delete");
	}

	@Override
	public void showSaveButtonDialog() {
		DialogFragment saveFragment = new SaveButtonDialogFragment();
		saveFragment.show(getFragmentManager(), "save");
	}
	
	@Override
	public boolean isShareIntent() {
		return false;
	}
}
