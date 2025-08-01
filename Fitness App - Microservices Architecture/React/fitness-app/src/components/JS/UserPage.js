import React, { useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode";
import { useNavigate } from "react-router-dom";
import "../CSS/UserPage.css";

import emailIcon from '../Images/email.PNG';
import passwordIcon from '../Images/password.PNG';
import ageIcon from '../Images/age.PNG';
import heightIcon from '../Images/height.PNG';
import weightIcon from '../Images/weight.PNG';
import activityLevelIcon from '../Images/activity_level.PNG';

import profileIcon from '../Images/profile.PNG';
import fitnessPlanIcon from '../Images/fitness_plan.PNG';
import dishesIcon from '../Images/dishes.PNG';
import exercisesIcon from '../Images/exercises.PNG';
import settingsIcon from '../Images/settings.PNG';

const UserPage = () => {
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [personalData, setPersonalData] = useState([]);
  const [fitnessPlan, setFitnessPlan] = useState([]);
  const [dishes, setDishes] = useState([]);
  const [exercises, setExercises] = useState([]);
  const [idToExerciseName, setIdToExerciseName] = useState({});
  const [idToDishName, setIdToDishName] = useState({});
  const [view, setView] = useState('profile'); // State to manage the view in MainContent
  const [selectedDay, setSelectedDay] = useState(null); // Adăugat pentru a urmări ziua selectată

  const handleLogout = async () => {
    try {
      const token = localStorage.getItem("token");
      
      if (!token) {
        console.error("Token not found");
        navigate("/login");
        return;
      }
      
      const response = await fetch("http://localhost:8081/logout", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ token }),
      });
      
      if (response.ok) {
        console.log("Logout successful");
        localStorage.removeItem("token");
        setIsLoggedIn(false);
        navigate("/login");
      } else {
        console.error("Failed to logout");
      }
      
    } catch (error) {
      console.error("Error during logout:", error);
    }
  };

  useEffect(() => {
    
    const fetchUserData = async () => {
      try {

        const token = localStorage.getItem("token");
        if (!token) {
          console.error("Token not found");
          setIsLoggedIn(false);
          navigate("/login");
          return;
        }
        
        const decodedToken = jwtDecode(token);

        const id_user = decodedToken.jti;
        console.log(decodedToken);
        if (!id_user || decodedToken.role !== "user") {
          console.error("Unauthorized access");
          setIsLoggedIn(false);
          navigate("/login");
          return;
        }

        setIsLoggedIn(true);

        const response = await fetch(`http://localhost:8081/api/fitness/users?id_user=${id_user}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        });

        if (response.ok) {
          const data = await response.json();
          setPersonalData(data);
          console.log(data);
          await fetchFitnessPlanData(id_user);
          await fetchDishesData();
          await fetchExercisesData();
        } else {
          console.error("Failed to fetch user data");
        }
      } catch (error) {
        console.error("Error fetching user data:", error);
      }
    };

    fetchUserData();

  }, [navigate]);

  const fetchFitnessPlanData = async (id_user) => {
    try {
      const response = await fetch(
        `http://localhost:8081/api/fitness/plan?id_user=${id_user}`
      );

      if (response.ok) {
        const data = await response.json();
        console.log(data[0]);
        setFitnessPlan(data[0]);
      } else if (response.status === 404) {
        setFitnessPlan(null);
      } else {
        console.error("Failed to fetch plan");
      }
    } catch (error) {
      console.error("Error fetching plan:", error);
    }
  };

  const fetchDishesData = async () => {
    try {
      const response = await fetch(
        "http://localhost:8081/api/fitness/dishes"
      );

      if (response.ok) {
        const data = await response.json();
        setDishes(data);

        const newIdToDishName = {};
        data.forEach(dish => {
          newIdToDishName[dish._id] = dish.name;
        });
        setIdToDishName(newIdToDishName);
      } else {
        console.error("Failed to dishes list");
      }
    } catch (error) {
      console.error("Error fetching dishes list:", error);
    }
  };

  const fetchExercisesData = async () => {
    try {
      const response = await fetch(
        "http://localhost:8081/api/fitness/exercises"
      );

      if (response.ok) {
        const data = await response.json();
        console.log(data);
        setExercises(data);

        const newIdToExerciseName = {};
        data.forEach(exercise => {
          newIdToExerciseName[exercise._id] = exercise.name;
        });
        setIdToExerciseName(newIdToExerciseName);
      } else {
        console.error("Failed to dishes list");
      }
    } catch (error) {
      console.error("Error fetching dishes list:", error);
    }
  };

  const setupNewPlan = async (id_user) => {
    try {
      const response = await fetch(`http://localhost:8081/api/fitnessplan/user?id_user=${id_user}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ /* additional plan setup data if needed */ }),
      });

      if (response.ok) {
        await fetchFitnessPlanData(id_user);
      } else {
        console.error("Failed to set up a new plan");
      }
    } catch (error) {
      console.error("Error setting up a new plan:", error);
    }
  };

  const handleDownloadRecipe = async (recipe) => {
    try {
      console.log(recipe);
      
      const encodeArray = (arr) => arr.join("\n");

      const encodedRecipe = {
          ingredients: encodeArray(recipe.ingredients),
          additional_preparations: encodeArray(recipe.additional_preparations),
          method: encodeArray(recipe.method)
      };

      // Serializăm obiectul encoded recipe într-un query string
      const queryString = new URLSearchParams(encodedRecipe).toString();
      
      const response = await fetch(
        `http://localhost:8081/api/fitness/dishes/generate_recipe?${queryString}`
      );
  
      if (response.ok) {
        console.log('Recipe PDF generated successfully.');
  
        // convertire raspunsul la format Blob
        const blob = await response.blob();
  
        // creare obiect URL pentru Blob
        const url = URL.createObjectURL(blob);
  
        // creare element de link pentru descărcare
        const a = document.createElement('a');
        a.href = url;
        a.download = 'recipe.pdf';
        document.body.appendChild(a);
        a.click();
  
        //eliminare obiect URL creat
        URL.revokeObjectURL(url);
  
      } else {
        console.error('Failed to generate recipe PDF.');
      }
    } catch (error) {
      console.error('Error generating recipe PDF:', error);
    }
  };

  const renderFitnessPlan = () => {
    if (fitnessPlan) {
      return (
        <div>
          <h2>Fitness plan</h2>
          <div className="calendar-container">
            <div className="calendar">
              {Array.from({ length: 30 }, (_, index) => (
                <div 
                  key={index + 1} 
                  className="calendar-day" 
                  onClick={() => setSelectedDay(index + 1)}
                  style={{ 
                    border: selectedDay === index + 1 ? '2px solid blue' : '1px solid black' 
                  }}
                >
                  {index + 1}
                </div>
              ))}
            </div>
            {selectedDay && fitnessPlan.exercises[selectedDay] && (
              <div className="day-details">
                <h3>Details for day {selectedDay}</h3>
                <p><strong>Exercises:</strong> {fitnessPlan.exercises[selectedDay].map(id => idToExerciseName[id]).join(', ')}</p>
                <h4>Dishes:</h4>
                <ul>
                  <li><strong>Breakfast:</strong> {idToDishName[fitnessPlan.dishes[selectedDay].breakfast]}</li>
                  <li><strong>Lunch:</strong> {idToDishName[fitnessPlan.dishes[selectedDay].lunch]}</li>
                  <li><strong>Dinner:</strong> {idToDishName[fitnessPlan.dishes[selectedDay].dinner]}</li>
                </ul>
              </div>
            )}
          </div>
        </div>
      );
    } else {
      return (
        <div className="fitnessButton-container">
          <button onClick={() => setupNewPlan(personalData.id_user)}>Set up a new plan!</button>
        </div>
      );
    }
  };
  

  const renderContent = () => {
    switch (view) {
      case 'profile':
        return (
          <div>
            <h2>Personal data</h2>
            <div className="user-info">
              <div className="info-row1">
                <div className="info-item">
                  <img src={emailIcon} alt="Email Icon" className="icon" />
                  <span>{personalData.email}</span>
                </div>
                <div className="info-item">
                  <img src={passwordIcon} alt="Password Icon" className="icon" />
                  <span>{personalData.password}</span>
                </div>
                <div className="info-item">
                  <img src={ageIcon} alt="Age Icon" className="icon2" />
                  <span>{personalData.age}</span>
                </div>
              </div>
              <div className="info-row2">
                <div className="info-item">
                  <img src={heightIcon} alt="Height Icon" className="icon" />
                  <span>{personalData.height + "cm"}</span>
                </div>
                <div className="info-item">
                  <img src={weightIcon} alt="Weight Icon" className="icon" />
                  <span>{personalData.weight + "kg"}</span>
                </div>
                <div className="info-item2">
                  <img src={activityLevelIcon} alt="Activity Level Icon" className="icon" />
                  <span>{personalData.activity_level}</span>
                </div>
              </div>
            </div>
          </div>
        );
      case 'fitness_plan':
        return renderFitnessPlan();
      case 'dishes':
      return (
        <div>
          <h2>Dishes</h2>
          {dishes.length > 0 ? (
            dishes.map((dish, index) => (
              <div key={index} className="dish-container">
                <h3>{dish.name}</h3>
                <div className="details-container">
                  <div className="dish-details">
                    <p>Meal type: {dish.meal_type}</p>
                    <p>Cooking difficulty: {dish.cooking_difficulty}</p>
                    <p>Preparation time: {dish.preparation_time}</p>
                  </div>
                  {dish.nutrition_per_serving && (
                    <div className="nutrition-details">
                      <h4>Nutrition per serving</h4>
                      <ul>
                        <li>kcal: {dish.nutrition_per_serving.kcal}</li>
                        <li>fat: {dish.nutrition_per_serving.fat}g</li>
                        <li>saturates: {dish.nutrition_per_serving.saturates}g</li>
                        <li>carbs: {dish.nutrition_per_serving.carbs}g</li>
                        <li>sugars: {dish.nutrition_per_serving.sugars}g</li>
                        <li>fibre: {dish.nutrition_per_serving.fibre}g</li>
                        <li>protein: {dish.nutrition_per_serving.protein}g</li>
                        <li>salt: {dish.nutrition_per_serving.salt}g</li>
                      </ul>
                    </div>
                  )}
                </div>
                <div className="recipe-actions">
                  <button onClick={() => handleDownloadRecipe(dish.recipe)}>Download Recipe</button>
                </div>
              </div>
            ))
          ) : (
            <p>No dishes found!</p>
          )}
        </div>
      );
      case 'exercises':
        return (
          <div>
            <h2>Exercises</h2>
            {exercises.length > 0 ? (
              <ul>
                {exercises.map((exercise, index) => (
                  <li key={index} className="exercise-item">
                    <div className="exercise-header">
                      <h3>{exercise.name}</h3>
                      <span className="exercise-details">
                        - Target muscle: {exercise.target_muscle}, 
                        Calories burned (male): {exercise.calories_burned_male} kcal/min,
                        Calories burned (female): {exercise.calories_burned_female} kcal/min
                      </span>
                    </div>
                  </li>
                ))}
              </ul>
            ) : (
              <p>No exercises found!</p>
            )}
          </div>
        );
      case 'settings':
        return (
          <div className="settings">
            <button onClick={() => navigate("/update-password")}>Update Password</button>
            <button onClick={handleLogout}>Log Out</button>
            <button style={{ backgroundColor: 'red' }} onClick={() => navigate("/delete-account")}>Delete Account</button>
          </div>
        );
      default:
        return null;
    }
  };

  const firstName = personalData.first_name || "*";

  if (!isLoggedIn) {
    return <div>Acces neautorizat.</div>;
  }
  else{
    return (
      <div className="container2">
        <header className="header">
          <h1>Fitness App</h1>
          <div className="initial-circle">{firstName[0].toUpperCase()}</div>
        </header>
        <div className="main-body">
          <aside className="left-sidebar">
            <button onClick={() => setView('profile')}>
              <img src={profileIcon} alt="Profile Icon" className="button-icon" />
              Profile
            </button>
            <button onClick={() => setView('fitness_plan')}>
              <img src={fitnessPlanIcon} alt="Fitness Plan Icon" className="button-icon" />
              Fitness Plan
            </button>
            <button onClick={() => setView('dishes')}>
              <img src={dishesIcon} alt="Dishes Icon" className="button-icon" />
              Dishes
            </button>
            <button onClick={() => setView('exercises')}>
              <img src={exercisesIcon} alt="Exercises Icon" className="button-icon" />
              Exercises
            </button>
            <button onClick={() => setView('settings')}>
              <img src={settingsIcon} alt="Settings Icon" className="button-icon" />
              Settings
            </button>
          </aside>
          <main className="main-content">
            {renderContent()}
          </main>
        </div>
      </div>
    );
  };
}

export default UserPage;