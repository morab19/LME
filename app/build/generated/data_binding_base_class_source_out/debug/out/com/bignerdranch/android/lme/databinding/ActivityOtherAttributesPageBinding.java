// Generated by view binder compiler. Do not edit!
package com.bignerdranch.android.lme.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.bignerdranch.android.lme.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityOtherAttributesPageBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView attributeTextView;

  @NonNull
  public final TextView attributeTextViewInfo;

  @NonNull
  public final TextView currentList;

  @NonNull
  public final Spinner endingHalfHourSpinner;

  @NonNull
  public final Spinner endingHourSpinner;

  @NonNull
  public final Spinner endingTimeOfDaySpinner;

  @NonNull
  public final EditText nameOfEventEditText;

  @NonNull
  public final Button nextButton;

  @NonNull
  public final Spinner startingHalfHourSpinner;

  @NonNull
  public final Spinner startingHourSpinner;

  @NonNull
  public final Spinner startingTimeOfDaySpinner;

  @NonNull
  public final TextView timeSchedule;

  private ActivityOtherAttributesPageBinding(@NonNull LinearLayout rootView,
      @NonNull TextView attributeTextView, @NonNull TextView attributeTextViewInfo,
      @NonNull TextView currentList, @NonNull Spinner endingHalfHourSpinner,
      @NonNull Spinner endingHourSpinner, @NonNull Spinner endingTimeOfDaySpinner,
      @NonNull EditText nameOfEventEditText, @NonNull Button nextButton,
      @NonNull Spinner startingHalfHourSpinner, @NonNull Spinner startingHourSpinner,
      @NonNull Spinner startingTimeOfDaySpinner, @NonNull TextView timeSchedule) {
    this.rootView = rootView;
    this.attributeTextView = attributeTextView;
    this.attributeTextViewInfo = attributeTextViewInfo;
    this.currentList = currentList;
    this.endingHalfHourSpinner = endingHalfHourSpinner;
    this.endingHourSpinner = endingHourSpinner;
    this.endingTimeOfDaySpinner = endingTimeOfDaySpinner;
    this.nameOfEventEditText = nameOfEventEditText;
    this.nextButton = nextButton;
    this.startingHalfHourSpinner = startingHalfHourSpinner;
    this.startingHourSpinner = startingHourSpinner;
    this.startingTimeOfDaySpinner = startingTimeOfDaySpinner;
    this.timeSchedule = timeSchedule;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityOtherAttributesPageBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityOtherAttributesPageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_other_attributes_page, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityOtherAttributesPageBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.attribute_text_view;
      TextView attributeTextView = ViewBindings.findChildViewById(rootView, id);
      if (attributeTextView == null) {
        break missingId;
      }

      id = R.id.attribute_text_view_info;
      TextView attributeTextViewInfo = ViewBindings.findChildViewById(rootView, id);
      if (attributeTextViewInfo == null) {
        break missingId;
      }

      id = R.id.current_list;
      TextView currentList = ViewBindings.findChildViewById(rootView, id);
      if (currentList == null) {
        break missingId;
      }

      id = R.id.ending_half_hour_spinner;
      Spinner endingHalfHourSpinner = ViewBindings.findChildViewById(rootView, id);
      if (endingHalfHourSpinner == null) {
        break missingId;
      }

      id = R.id.ending_hour_spinner;
      Spinner endingHourSpinner = ViewBindings.findChildViewById(rootView, id);
      if (endingHourSpinner == null) {
        break missingId;
      }

      id = R.id.ending_time_of_day_spinner;
      Spinner endingTimeOfDaySpinner = ViewBindings.findChildViewById(rootView, id);
      if (endingTimeOfDaySpinner == null) {
        break missingId;
      }

      id = R.id.name_of_event_edit_text;
      EditText nameOfEventEditText = ViewBindings.findChildViewById(rootView, id);
      if (nameOfEventEditText == null) {
        break missingId;
      }

      id = R.id.next_button;
      Button nextButton = ViewBindings.findChildViewById(rootView, id);
      if (nextButton == null) {
        break missingId;
      }

      id = R.id.starting_half_hour_spinner;
      Spinner startingHalfHourSpinner = ViewBindings.findChildViewById(rootView, id);
      if (startingHalfHourSpinner == null) {
        break missingId;
      }

      id = R.id.starting_hour_spinner;
      Spinner startingHourSpinner = ViewBindings.findChildViewById(rootView, id);
      if (startingHourSpinner == null) {
        break missingId;
      }

      id = R.id.starting_time_of_day_spinner;
      Spinner startingTimeOfDaySpinner = ViewBindings.findChildViewById(rootView, id);
      if (startingTimeOfDaySpinner == null) {
        break missingId;
      }

      id = R.id.time_schedule;
      TextView timeSchedule = ViewBindings.findChildViewById(rootView, id);
      if (timeSchedule == null) {
        break missingId;
      }

      return new ActivityOtherAttributesPageBinding((LinearLayout) rootView, attributeTextView,
          attributeTextViewInfo, currentList, endingHalfHourSpinner, endingHourSpinner,
          endingTimeOfDaySpinner, nameOfEventEditText, nextButton, startingHalfHourSpinner,
          startingHourSpinner, startingTimeOfDaySpinner, timeSchedule);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
