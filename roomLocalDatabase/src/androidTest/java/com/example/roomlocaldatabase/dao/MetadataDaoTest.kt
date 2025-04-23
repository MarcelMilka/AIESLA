package com.example.roomlocaldatabase.dao

import android.database.sqlite.SQLiteConstraintException
import com.example.roomlocaldatabase.db.RoomLocalDatabase
import com.example.roomlocaldatabase.entity.MetadataEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MetadataDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var room: RoomLocalDatabase

    @Before
    fun before() {

        hiltRule.inject()
    }

    @After
    fun after() {

        room.clearAllTables()
        room.close()
    }

    private val defaultMetadataEntity = MetadataEntity(
        index = 0,
        relatedUUID = null,
        signedIn = true
    )

    private val uuid = "p55eDUn1ulUXaQa2"

    private val metadataEntityWithUUID = MetadataEntity(
        index = 0,
        relatedUUID = this.uuid,
        signedIn = true
    )

    @Test
    fun `getMetadata - returns null when row with column index equal to 0 does not exist`() = runTest {

        assertNull(room.metadataDAO().getMetadata())
    }

    @Test
    fun `getMetadata - returns MetadataEntity when row with index equal to 0 exists`() = runTest {

        // insert instance of MetadataEntity to the table 'Metadata'
        room.metadataDAO().initializeMetadata(
            metadataEntity = defaultMetadataEntity
        )

        // make sure the tested method returns instance of MetadataEntity
        assertEquals(
            defaultMetadataEntity,
            room.metadataDAO().getMetadata()
        )
    }



    @Test
    fun `initializeMetadata - properly adds instance of MetadataEntity into the table Metadata`() = runTest {

        // make sure the table 'Metadata' does not contain any rows
        assertNull(room.metadataDAO().getMetadata())

        // call the tested method
        room.metadataDAO().initializeMetadata(
            metadataEntity = defaultMetadataEntity
        )

        // make sure the row has been properly added to the table 'Metadata'
        assertEquals(
            defaultMetadataEntity,
            room.metadataDAO().getMetadata()
        )
    }

    @Test
    fun `initializeMetadata - aborts transaction in case conflict`() = runTest {

        // insert instance of MetadataEntity to the table 'Metadata'
        room.metadataDAO().initializeMetadata(
            metadataEntity = defaultMetadataEntity
        )

        // Expect SQLiteConstraintException when inserting the same entity again
        assertThrows(SQLiteConstraintException::class.java) {

            runBlocking {
                room.metadataDAO().initializeMetadata(
                    metadataEntity = defaultMetadataEntity
                )
            }
        }

        // make sure the first instance still exists
        assertEquals(
            defaultMetadataEntity,
            room.metadataDAO().getMetadata()
        )
    }



    @Test
    fun `assignRelatedUUID - properly updates the column relatedUUID`() = runTest {

        // insert instance of MetadataEntity to the table 'Metadata'
        room.metadataDAO().initializeMetadata(
            metadataEntity = defaultMetadataEntity
        )

        // call the tested method
        room.metadataDAO().assignRelatedUUID(uuid = uuid)

        // make sure the UUID has been updated
        assertEquals(
            metadataEntityWithUUID,
            room.metadataDAO().getMetadata()
        )
    }



    @Test
    fun `isSignedIn - works as intended`() = runTest {

        // initialize metadata
        room.metadataDAO().initializeMetadata(
            metadataEntity = defaultMetadataEntity
        )

        // make sure the tested method returns true
        assertTrue(room.metadataDAO().isSignedIn())

        // sign out and make sure the tested method returns false
        room.metadataDAO().signOut()
        assertFalse(room.metadataDAO().isSignedIn())

        // sign in and make sure the tested method returns true
        room.metadataDAO().signIn()
        assertTrue(room.metadataDAO().isSignedIn())
    }

    @Test
    fun `isSignedIn - returns false when row with index equal to 0 does not exist`() = runTest {

        assertFalse(room.metadataDAO().isSignedIn())
    }



    @Test
    fun `signIn - properly sets the field signedIn to true`() = runTest {

        // initialize metadata
        room.metadataDAO().initializeMetadata(
            metadataEntity = defaultMetadataEntity
        )

        // make sure 'signedIn' is true by default
        assertTrue(room.metadataDAO().isSignedIn())

        // signOut and make sure 'signedIn' is equal to false
        room.metadataDAO().signOut()
        assertFalse(room.metadataDAO().isSignedIn())

        // call the tested method and check if the field signedIn is equal to true
        room.metadataDAO().signIn()
        assertTrue(room.metadataDAO().isSignedIn())
    }

    @Test
    fun `signIn - calling the method signIn more than once in a row does not cause errors`() = runTest {

        // initialize metadata
        room.metadataDAO().initializeMetadata(
            metadataEntity = defaultMetadataEntity
        )

        // call signIn more than once
        room.metadataDAO().signIn()
        room.metadataDAO().signIn()
        assertTrue(room.metadataDAO().isSignedIn())

        // sign out and make sure 'signedIn' is equal to false
        room.metadataDAO().signOut()
        assertFalse(room.metadataDAO().isSignedIn())
    }



    @Test
    fun `signOut - properly sets the field signedIn to false`() = runTest {

        // initialize metadata
        room.metadataDAO().initializeMetadata(
            metadataEntity = defaultMetadataEntity
        )

        // call the tested method and check if the field 'signedIn' is equal to false
        room.metadataDAO().signOut()
        assertFalse(room.metadataDAO().isSignedIn())
    }

    @Test
    fun `signOut - calling the method signOut more than once in a row does not cause errors`() = runTest {

        // initialize metadata
        room.metadataDAO().initializeMetadata(
            metadataEntity = defaultMetadataEntity
        )

        // call signOut more than once
        room.metadataDAO().signOut()
        room.metadataDAO().signOut()
        assertFalse(room.metadataDAO().isSignedIn())

        // sign in and make sure 'signedIn' is equal to true
        room.metadataDAO().signIn()
        assertTrue(room.metadataDAO().isSignedIn())
    }
}