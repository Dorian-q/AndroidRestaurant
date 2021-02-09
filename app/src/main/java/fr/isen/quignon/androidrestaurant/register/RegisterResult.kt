package fr.isen.quignon.androidrestaurant.register

import java.io.Serializable

class RegisterResult(val data: User) {}

class User(val id: Int) : Serializable {}