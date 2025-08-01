import React, { useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode";
import { useNavigate } from "react-router-dom";
import "../CSS/AdminPage.css";

import dashboardIcon from '../Images/dash_board.PNG';
import dishesIcon from '../Images/dishes.PNG';
import exercisesIcon from '../Images/exercises.PNG';
import usersListIcon from '../Images/users_list.PNG';
import settingsIcon from '../Images/settings.PNG';

const AdminPage = () => {
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [personalData, setPersonalData] = useState([]);
  const [dishes, setDishes] = useState([]);
  const [exercises, setExercises] = useState([]);
  const [users, setUsers] = useState([]);
  const [fitnessPlans, setFitnessPlans] = useState([]);
  const [view, setView] = useState('dashboard'); // State to manage the view in MainContent
  const [addingExercise, setAddingExercise] = useState(false); // State to manage adding new exercise
  const [addingDish, setAddingDish] = useState(false);
  const [newExercise, setNewExercise] = useState({
    name: '',
    target_muscle: '',
    calories_burned_male: '',
    calories_burned_female: '',
  });

  const [newDish, setNewDish] = useState({
    name: '',
    meal_type: '',
    cooking_difficulty: '',
    preparation_time: '',
    nutrition_per_serving: {
      kcal: '',
      fat: '',
      saturates: '',
      carbs: '',
      sugars: '',
      fibre: '',
      protein: '',
      salt: '',
    },
    recipe: {
      ingredients: [],
      additional_preparations: [],
      method: [],
    }
  });

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
    const fetchData = async () => {
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

        if (!id_user || decodedToken.role !== "admin") {
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

          await fetchDishesData();
          await fetchExercisesData();
          await fetchUsersData();
          await fetchFitnessPlansData();
        } else {
          console.error("Failed to fetch admin data");
        }
      } catch (error) {
        console.error("Error fetching admin data:", error);
      }
    };

    fetchData();
  }, [navigate]);


  const fetchDishesData = async () => {
    try {
      const response = await fetch(
        "http://localhost:8081/api/fitness/dishes"
      );

      if (response.ok) {
        const data = await response.json();
        setDishes(data);
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
        } else {
          console.error("Failed to dishes list");
        }
      } catch (error) {
        console.error("Error fetching dishes list:", error);
      }
    };

    const fetchUsersData = async () => {
      try {
        const response = await fetch(
          "http://localhost:8081/api/fitness/users"
        );

        if (response.ok) {
          const data = await response.json();
          setUsers(data);
        } else {
          console.error("Failed to users list");
        }
      } catch (error) {
        console.error("Error fetching users list:", error);
      }
    };

    const fetchFitnessPlansData = async () => {
      try {
        const response = await fetch(
          `http://localhost:8081/api/fitness/plan`
        );
  
        if (response.ok) {
          const data = await response.json();
          console.log(data);
          setFitnessPlans(data);
        } else {
          console.error("Failed to fetch fitness plans list");
        }
      } catch (error) {
        console.error("Error fetching fitness plans list:", error);
      }
    };

    const handleAddNewDish = async () => {
      try {
        console.log(newDish);
        const response = await fetch("http://localhost:8081/api/fitness/dishes", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(newDish),
        });
  
        if (response.ok) {
          console.log('Dish added successfully.');
          setAddingDish(false);
          await fetchDishesData(); // actualizez lista cu preparate
          setNewDish({
            name: '',
            meal_type: '',
            cooking_difficulty: '',
            preparation_time: '',
            nutrition_per_serving: {
              kcal: '',
              fat: '',
              saturates: '',
              carbs: '',
              sugars: '',
              fibre: '',
              protein: '',
              salt: '',
            }
          });
        } else {
          console.error('Failed to add a new dish');
        }
      } catch (error) {
        console.error('Error while adding a new dish:', error);
      }
    };

  const handleAddNewExercise = async () => {
    try {
      const response = await fetch("http://localhost:8081/api/fitness/exercises", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newExercise),
      });
  
      if (response.ok) {
        console.log('Exercise added successfully.');
        setAddingExercise(false);
        await fetchExercisesData(); // actualizez lista cu exercitii
        setNewExercise({
          name: '',
          target_muscle: '',
          calories_burned_male: '',
          calories_burned_female: '',
        });
      } else {
        console.error('Failed to add a new exercise');
      }
    } catch (error) {
      console.error('Error while adding a new exercise:', error);
    }
  };
  
  const renderContent = () => {
    switch (view) {
      case 'dashboard':
        return (
          <div className="header1">
            <h1>Welcome, admin!</h1>
          </div>
        );
        case 'dishes':
        return (
          <div>
            <div className="antet">
              <h2>Dishes</h2>
              {addingDish ? (
                <div className="new-dish-form">
                  <label>
                    Dish Name:
                    <input
                      type="text"
                      value={newDish.name}
                      onChange={(e) => setNewDish({ ...newDish, name: e.target.value })}
                      required
                    />
                  </label>
                  <label>
                    Meal Type:
                    <input
                      type="text"
                      value={newDish.meal_type}
                      onChange={(e) => setNewDish({ ...newDish, meal_type: e.target.value })}
                      required
                    />
                  </label>
                  <label>
                    Cooking Difficulty:
                    <input
                      type="text"
                      value={newDish.cooking_difficulty}
                      onChange={(e) => setNewDish({ ...newDish, cooking_difficulty: e.target.value })}
                      required
                    />
                  </label>
                  <label>
                    Preparation Time:
                    <input
                      type="text"
                      value={newDish.preparation_time}
                      onChange={(e) => setNewDish({ ...newDish, preparation_time: e.target.value })}
                      required
                    />
                  </label>
                  <label>
                    Nutrition Per Serving:
                    <div className="nutrition-inputs">
                      <input 
                        type="text" 
                        placeholder="kcal"
                        value={newDish.nutrition_per_serving.kcal}
                        onChange={(e) => setNewDish({ 
                          ...newDish, 
                          nutrition_per_serving: { ...newDish.nutrition_per_serving, kcal: e.target.value } 
                        })}
                        required
                      />
                      <input 
                        type="text" 
                        placeholder="fat"
                        value={newDish.nutrition_per_serving.fat}
                        onChange={(e) => setNewDish({ 
                          ...newDish, 
                          nutrition_per_serving: { ...newDish.nutrition_per_serving, fat: e.target.value } 
                        })}
                        required
                      />
                      <input 
                        type="text" 
                        placeholder="saturates"
                        value={newDish.nutrition_per_serving.saturates}
                        onChange={(e) => setNewDish({ 
                          ...newDish, 
                          nutrition_per_serving: { ...newDish.nutrition_per_serving, saturates: e.target.value } 
                        })}
                        required
                      />
                      <input 
                        type="text" 
                        placeholder="carbs"
                        value={newDish.nutrition_per_serving.carbs}
                        onChange={(e) => setNewDish({ 
                          ...newDish, 
                          nutrition_per_serving: { ...newDish.nutrition_per_serving, carbs: e.target.value } 
                        })}
                        required
                      />
                      <input 
                        type="text" 
                        placeholder="sugars"
                        value={newDish.nutrition_per_serving.sugars}
                        onChange={(e) => setNewDish({ 
                          ...newDish, 
                          nutrition_per_serving: { ...newDish.nutrition_per_serving, sugars: e.target.value } 
                        })}
                        required
                      />
                      <input 
                        type="text" 
                        placeholder="fibre"
                        value={newDish.nutrition_per_serving.fibre}
                        onChange={(e) => setNewDish({ 
                          ...newDish, 
                          nutrition_per_serving: { ...newDish.nutrition_per_serving, fibre: e.target.value } 
                        })}
                        required
                      />
                      <input 
                        type="text" 
                        placeholder="protein"
                        value={newDish.nutrition_per_serving.protein}
                        onChange={(e) => setNewDish({ 
                          ...newDish, 
                          nutrition_per_serving: { ...newDish.nutrition_per_serving, protein: e.target.value } 
                        })}
                        required
                      />
                      <input 
                        type="text" 
                        placeholder="salt"
                        value={newDish.nutrition_per_serving.salt}
                        onChange={(e) => setNewDish({ 
                          ...newDish, 
                          nutrition_per_serving: { ...newDish.nutrition_per_serving, salt: e.target.value } 
                        })}
                        required
                      />
                    </div>
                  </label>
                  <label>
                    Recipe:
                    <div className="recipe-inputs">
                      <textarea 
                        placeholder="Ingredients"
                        value={newDish.recipe.ingredients.join('\n')}
                        onChange={(e) => setNewDish({ 
                          ...newDish, 
                          recipe: { ...newDish.recipe, ingredients: e.target.value.split('\n') } 
                        })}
                        required
                      />
                      <textarea 
                        placeholder="Additional Preparations"
                        value={newDish.recipe.additional_preparations.join('\n')}
                        onChange={(e) => setNewDish({ 
                          ...newDish, 
                          recipe: { ...newDish.recipe, additional_preparations: e.target.value.split('\n') } 
                        })}
                        required
                      />
                      <textarea 
                        placeholder="Method"
                        value={newDish.recipe.method.join('\n')}
                        onChange={(e) => setNewDish({ 
                          ...newDish, 
                          recipe: { ...newDish.recipe, method: e.target.value.split('\n') } 
                        })}
                        required
                      />
                    </div>
                  </label>
                  <button onClick={handleAddNewDish}>Add Dish</button>
                  <button onClick={() => setAddingDish(false)}>Cancel</button>
                </div>
              ) : (
                <div className="recipe-update">
                  <button onClick={() => setAddingDish(true)}>Add a new dish</button>
                </div>
              )}
            </div>
            {!addingDish && dishes.length > 0 ? (
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
                </div>
              ))
            ) : (
              !addingDish && <p>No dishes found!</p>
            )}
          </div>
        );
      case 'exercises':
        return (
          <div className="antet2">
            <h2>Exercises</h2>
            {addingExercise ? (
              <div className="new-exercise-form">
                <label>
                  Exercise Name:
                  <input 
                    type="text" 
                    value={newExercise.name}
                    onChange={(e) => setNewExercise({ ...newExercise, name: e.target.value })}
                    required
                  />
                </label>
                <label>
                  Target Muscle:
                  <input 
                    type="text" 
                    value={newExercise.target_muscle}
                    onChange={(e) => setNewExercise({ ...newExercise, target_muscle: e.target.value })}
                    required
                  />
                </label>
                <label>
                  Calories Burned (Male):
                  <input 
                    type="number"
                    step="0.1"
                    value={newExercise.calories_burned_male}
                    onChange={(e) => setNewExercise({ ...newExercise, calories_burned_male: e.target.value })}
                    required
                  />
                </label>
                <label>
                  Calories Burned (Female):
                  <input 
                    type="number"
                    step="0.1"
                    value={newExercise.calories_burned_female}
                    onChange={(e) => setNewExercise({ ...newExercise, calories_burned_female: e.target.value })}
                    required
                  />
                </label>
                <button onClick={handleAddNewExercise}>Add Exercise</button>
                <button onClick={() => setAddingExercise(false)}>Cancel</button>
              </div>
            ) : (
              <div className="new-exercise">
                <button onClick={() => setAddingExercise(true)}>Add a new exercise</button>
              </div>
            )}
            {!addingExercise && exercises.length > 0 ? (
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
              !addingExercise && <p>No exercises found!</p>
            )}
          </div>
        );
      case 'users':
        const usersWithFitnessPlan = users.filter(user => fitnessPlans.some(plan => plan.id_user === user.id_user));
        const usersWithoutFitnessPlan = users.filter(user => !fitnessPlans.some(plan => plan.id_user === user.id_user));
        
        return (
          <div>
            <h2>Users</h2>
            <table className="user-table">
              <thead>
                <tr>
                  <th>Users with fitness plan</th>
                  <th>Users without fitness plan</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>
                    <ul>
                      {usersWithFitnessPlan.map(user => (
                        <li key={user.id_user}>{user.first_name} {user.last_name} - {user.email}</li>
                      ))}
                    </ul>
                  </td>
                  <td>
                    <ul>
                      {usersWithoutFitnessPlan.map(user => (
                        <li key={user.id_user}>{user.first_name} {user.last_name} - {user.email}</li>
                      ))}
                    </ul>
                  </td>
                </tr>
              </tbody>
            </table>
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

  const firstName = personalData.first_name || ".";

  if (!isLoggedIn) {
    return <h2>Acces neautorizat!</h2>;
  }
  else{
    return (
      <div className="container3">
        <header className="header">
          <h1>Fitness App</h1>
          <div className="initial-circle">*</div>
        </header>
        <div className="main-body2">
          <aside className="left-sidebar">
            <button onClick={() => setView('dashboard')}>
              <img src={dashboardIcon} alt="Profile Icon" className="button-icon" />
              Dashboard
            </button>
            <button onClick={() => setView('dishes')}>
              <img src={dishesIcon} alt="Dishes Icon" className="button-icon" />
              Dishes
            </button>
            <button onClick={() => setView('exercises')}>
              <img src={exercisesIcon} alt="Exercises Icon" className="button-icon" />
              Exercises
            </button>
            <button onClick={() => setView('users')}>
              <img src={usersListIcon} alt="Fitness Plan Icon" className="button-icon" />
              Users List
            </button>
            <button onClick={() => setView('settings')}>
              <img src={settingsIcon} alt="Settings Icon" className="button-icon" />
              Settings
            </button>
          </aside>
          <main className="main-content2">
            {renderContent()}
          </main>
        </div>
      </div>
    );
  };
}

export default AdminPage;