import { User } from "../../auth/models/user.model";

export interface Recipe {
  id: number;
  titulo: string;
  descricao: string;
  ingredientes: string;
  pathImg: string; // Corrigido de imagePath para pathImg
  votos: any[]; // Alterado de likes para votos
  autor: User;
  autorNome?: string; // Adicionado 
  createdAt: string; // Adicionado
  updatedAt: string; // Adicionado
  isAuthor?: boolean; // ADDED
  hasVoted?: boolean; // ADDED
  totalVotos?: number; // ADDED
}
