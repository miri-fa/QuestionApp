package com.example.question2;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivityTeacherTest {

    @Mock
    private ListView mockListView;

    @Mock
    private ArrayAdapter<String> mockAdapter;

    @Mock
    private FirebaseUser mockUser;

    @Mock
    private DatabaseReference mockDatabaseReference;

    @Mock
    private Query mockQuery;

    private MainActivityTeacher mainActivityTeacher;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mainActivityTeacher = spy(new MainActivityTeacher());
        doReturn(mockListView).when(mainActivityTeacher).findViewById(R.id.teacher_main_list);
        doReturn(mockUser).when(FirebaseAuth.getInstance()).getCurrentUser();
        doReturn(mockDatabaseReference).when(FirebaseDatabase.getInstance()).getReference("questionnaires");
        doReturn(mockQuery).when(mockDatabaseReference).orderByChild("author");
    }

    @Test
    public void testQuestionnairesDisplayed() {
        ArrayList<String> mockArrayList = mock(ArrayList.class);
        when(mockArrayList.add(anyString())).thenReturn(true);
        when(mockListView.getItemAtPosition(anyInt())).thenReturn("Test Questionnaire");

        mainActivityTeacher.onCreate(null);

        verify(mockQuery).addValueEventListener(any(ValueEventListener.class));
        verify(mockListView).setAdapter(mockAdapter);
        verify(mockAdapter).notifyDataSetChanged();
    }
}
