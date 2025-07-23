package com.example.jpitts_finalgirl_test_3.repository

import com.example.jpitts_finalgirl_test_3.model.InventoryItem
import com.example.jpitts_finalgirl_test_3.model.StoryChoice
import com.example.jpitts_finalgirl_test_3.model.StoryPage

object StoryRepository {
    private val storyMap = mapOf(
        1 to StoryPage(1, """
            You wake up on the dusty floor of an old cabin deep in the woods.  
            The moonlight flickers through broken windows. You hear twigs snapping outside.
            Something is out there.
        """.trimIndent(), listOf(
            StoryChoice("Grab a knife and check outside", 2, itemToAdd = InventoryItem("Knife", "A rusty kitchen knife")),
            StoryChoice("Hide under the bed", 3)
        )),
        2 to StoryPage(2, """
            You quietly open the creaky door, gripping the knife tight.  
            Shadows move between the trees. A silhouette stands—tall, hulking, wearing a mask.
            It's the killer.
        """.trimIndent(), listOf(
            StoryChoice("Run back inside and lock the door", 4),
            StoryChoice("Try to scare it off", 5)
        )),
        3 to StoryPage(3, """
            You crawl under the bed, heart pounding.  
            The door handle rattles violently.
        """.trimIndent(), listOf(
            StoryChoice("Stay silent and wait", 6),
            StoryChoice("Make a run for the back door", 7)
        )),
        4 to StoryPage(4, """
            You slam the door shut and lock it.  
            The killer pounds on the door, but can’t break through... for now.  
            You need a plan.
        """.trimIndent(), listOf(
            StoryChoice("Look for a weapon", 8),
            StoryChoice("Call for help on your phone", 9)
        )),
        5 to StoryPage(5, """
            You scream and wave the knife.  
            The killer just laughs, advancing with a machete.  
            You’re too slow.
            \n\n**You didn’t survive the night. The end.**
        """.trimIndent(), emptyList()), // Ending

        6 to StoryPage(6, """
            You hold your breath.  
            The door handle finally stops rattling.  
            After what feels like hours, silence returns.
        """.trimIndent(), listOf(
            StoryChoice("Sneak out and run for the woods", 7),
            StoryChoice("Stay hidden and wait till dawn", 10)
        )),
        7 to StoryPage(7, """
            You bolt through the back door into the woods,  
            but the killer’s footsteps thunder behind you.  
            You trip on a root and fall.
        """.trimIndent(), listOf(
            StoryChoice("Fight back with a broken branch", 11),
            StoryChoice("Try to crawl away", 12)
        )),
        8 to StoryPage(8, """
            You find an old shotgun under a dusty blanket.  
            It’s loaded but hasn’t been fired in years.
        """.trimIndent(), listOf(
            StoryChoice("Grab the shotgun", 13, itemToAdd = InventoryItem("Shotgun", "Old but loaded")),
            StoryChoice("Decide to hide instead", 6)
        )),
        9 to StoryPage(9, """
            You dial 911, but there’s no signal.  
            The killer is right outside, growing impatient.
        """.trimIndent(), listOf(
            StoryChoice("Hang up and grab a weapon", 8),
            StoryChoice("Try to barricade the windows", 14)
        )),
        10 to StoryPage(10, """
            The sun rises, and the killer disappears into the woods.  
            You survived the night, but for how long?
            \n\n**You live to see another day. The end.**
        """.trimIndent(), emptyList()), //ending

        11 to StoryPage(11, """
            You swing the branch wildly. The killer shrugs it off and strikes.  
            Darkness takes you.
            \n\n**You did not survive. The end.**
        """.trimIndent(), emptyList()), //ending

        12 to StoryPage(12, """
            You crawl and hide behind a tree,  
            barely breathing. The killer searches but moves on.
        """.trimIndent(), listOf(
            StoryChoice("Keep crawling to safety", 10),
            StoryChoice("Try to climb a tree", 15)
        )),
        13 to StoryPage(13, """
            With shotgun in hand, you face the door.  
            The killer bursts in. You fire!
        """.trimIndent(), listOf(
            StoryChoice("Kill the killer", 16),
            StoryChoice("Miss and run", 7)
        )),
        14 to StoryPage(14, """
            You pile furniture against the windows.  
            The killer starts breaking in.
        """.trimIndent(), listOf(
            StoryChoice("Fight at the door", 13),
            StoryChoice("Try to escape through the chimney", 17)
        )),
        15 to StoryPage(15, """
            You climb up, but the branch snaps.  
            The killer catches you.
            **You did not survive. The end.**
        """.trimIndent(), emptyList()), //ending

        16 to StoryPage(16, """
            Your shotgun blast hits the killer,  
            and they collapse. You’ve won... for now.
            **You survived. The end.**
        """.trimIndent(), emptyList()), //ending

        17 to StoryPage(17, """
            You squeeze through the chimney, smoke stinging your eyes.  
            You escape into the woods and hear sirens in the distance.
            **You survived and got rescued. The end.**
        """.trimIndent(), emptyList()) //ending
    )

    fun getPage(id: Int): StoryPage? = storyMap[id]
}
