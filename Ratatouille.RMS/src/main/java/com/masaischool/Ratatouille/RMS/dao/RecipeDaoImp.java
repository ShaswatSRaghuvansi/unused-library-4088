package com.masaischool.Ratatouille.RMS.dao;

import java.util.List;

import com.masaischool.Ratatouille.RMS.Utility.EMUtils;

import com.masaischool.Ratatouille.RMS.entity.Recipe;
import com.masaischool.Ratatouille.RMS.exception.NoRecordFoundException;
import com.masaischool.Ratatouille.RMS.exception.SomeThingWentWrongException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;

public class RecipeDaoImp implements RecipeDao {

    @Override
    public List<Recipe> viewAllRecipe() throws SomeThingWentWrongException, NoRecordFoundException {
        EntityManager em = null;
        List<Recipe> recipeList = null;
        try {
            em = EMUtils.getEntityManager();

            // Retrieve a list of all recipes
            Query query = em.createQuery("FROM Recipe r where r.isDeleted=0");
            recipeList = (List<Recipe>) query.getResultList();

            // Check if any recipes were found
            if (recipeList.size() == 0) {
                throw new NoRecordFoundException("No Recipe Found");
            }
        } catch (IllegalArgumentException ex) {
            throw new SomeThingWentWrongException("Unable to process request, try again later");
        } finally {
            em.close();
        }
        return recipeList;
    }

    @Override
    public List<Recipe> viewRecipesWithGivenIngredients(String ingredients) throws SomeThingWentWrongException, NoRecordFoundException {
        EntityManager em = null;
        List<Recipe> recipeList = null;
        try {
            em = EMUtils.getEntityManager();

            // Retrieve a list of recipes containing the specified ingredients
            Query query = em.createQuery("FROM Recipe r WHERE r.ingredients LIKE :ingredient AND r.isDeleted = 0");
            query.setParameter("ingredient", "%" + ingredients + "%");

            recipeList = (List<Recipe>) query.getResultList();

            // Check if any recipes were found
            if (recipeList.size() == 0) {
                throw new NoRecordFoundException("No Recipe Found");
            }
        } catch (IllegalArgumentException ex) {
            throw new SomeThingWentWrongException("Unable to process request, try again later");
        } finally {
            em.close();
        }
        return recipeList;
    }

    @Override
    public Recipe getRecipeByID(int id) throws SomeThingWentWrongException, NoRecordFoundException {
        EntityManager em = null;
        Recipe recipe = null;
        try {
            em = EMUtils.getEntityManager();

            // Retrieve a specific recipe by its ID
            Query query = em.createQuery("SELECT c FROM Recipe c WHERE recipeId = :id AND isDeleted = 0");
            query.setParameter("id", id);
            recipe = (Recipe) query.getSingleResult();

            // Check if the recipe with the given ID exists
            if (recipe == null) {
                throw new NoRecordFoundException("The Recipe With the Given Id does not Exist");
            }
        } catch (PersistenceException ex) {
            throw new SomeThingWentWrongException("Unable to process request, try again later");
        } finally {
            em.close();
        }
        return recipe;
    }
}
