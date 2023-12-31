package com.example.rawggamebase

import com.example.rawggamebase.features.detail.GameDetailViewModelTest
import com.example.rawggamebase.features.list.ListViewModelTest
import com.example.rawggamebase.features.main.MainViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    MainViewModelTest::class,
    GameDetailViewModelTest::class,
    ListViewModelTest::class,
)
class SuiteTest