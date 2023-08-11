import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PropertyTypeComponent } from './list/property-type.component';
import { PropertyTypeDetailComponent } from './detail/property-type-detail.component';
import { PropertyTypeUpdateComponent } from './update/property-type-update.component';
import PropertyTypeResolve from './route/property-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const propertyTypeRoute: Routes = [
  {
    path: '',
    component: PropertyTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PropertyTypeDetailComponent,
    resolve: {
      propertyType: PropertyTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PropertyTypeUpdateComponent,
    resolve: {
      propertyType: PropertyTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PropertyTypeUpdateComponent,
    resolve: {
      propertyType: PropertyTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default propertyTypeRoute;
