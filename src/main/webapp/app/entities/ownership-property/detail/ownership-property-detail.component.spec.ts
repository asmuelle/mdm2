import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OwnershipPropertyDetailComponent } from './ownership-property-detail.component';

describe('OwnershipProperty Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OwnershipPropertyDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: OwnershipPropertyDetailComponent,
              resolve: { ownershipProperty: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(OwnershipPropertyDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load ownershipProperty on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OwnershipPropertyDetailComponent);

      // THEN
      expect(instance.ownershipProperty).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
