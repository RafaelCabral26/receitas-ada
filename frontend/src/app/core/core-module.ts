import { CommonModule } from '@angular/common';
import { NavbarComponent } from './components/navbar/navbar';
import { NgModule } from '@angular/core';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    NavbarComponent
  ],
  exports: [
    NavbarComponent
  ]
})
export class CoreModule { }

